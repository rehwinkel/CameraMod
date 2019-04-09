package com.deerangle.block;

import com.deerangle.camera.CameraRenderGlobal;
import com.deerangle.camera.EntityCamera;
import com.deerangle.main.ContentLoader;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderTelevision extends TileEntitySpecialRenderer<TileEntityTelevision> {

    public static final int quality = 128;
    private static final double px = 1.0 / 16.0;
    private static final List<BlockPos> channelsInView = new CopyOnWriteArrayList<>();
    private static Minecraft mc = Minecraft.getMinecraft();
    private static long renderEndNanoTime = 0;

    private static CameraRenderGlobal cameraRenderGlobal = new CameraRenderGlobal(mc);
    private static boolean rendering;
    private static EntityPlayer renderEntity;
    private Entity backupEntity;

    public static void renderWorld(int fbo, BlockPos position, EnumFacing direction, Minecraft mc, float partialTicks) {
        EntityCamera entity = new EntityCamera(mc.world, position.down(), direction);

        GameSettings settings = mc.gameSettings;
        Entity entityBackup = mc.getRenderViewEntity();
        int thirdPersonBackup = settings.thirdPersonView;
        boolean hideGuiBackup = settings.hideGUI;
        int mipmapBackup = settings.mipmapLevels;
        float fovBackup = settings.fovSetting;
        int widthBackup = mc.displayWidth;
        int heightBackup = mc.displayHeight;
        boolean fboBackup = settings.fboEnable;
        boolean anaglyphBackup = settings.anaglyph;
        RenderGlobal renderBackup = mc.renderGlobal;

        mc.setRenderViewEntity(entity);
        settings.fovSetting = 110f;
        settings.thirdPersonView = 0;
        settings.hideGUI = true;
        settings.mipmapLevels = 3;
        mc.displayWidth = quality;
        mc.displayHeight = quality;
        settings.fboEnable = true;
        settings.anaglyph = false;
        mc.renderGlobal = cameraRenderGlobal;

        rendering = true;
        renderEntity = mc.player;

        int fps = 8;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        EntityRenderer entityRenderer = mc.entityRenderer;
        //getFOVModifier
        //getFOVOffset
        entityRenderer.renderWorld(1, renderEndNanoTime + (1000000000 / fps));
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        renderEndNanoTime = System.nanoTime();

        renderEntity = null;
        rendering = false;

        mc.setRenderViewEntity(entityBackup);
        settings.fovSetting = fovBackup;
        settings.thirdPersonView = thirdPersonBackup;
        settings.hideGUI = hideGuiBackup;
        settings.mipmapLevels = mipmapBackup;
        settings.fboEnable = fboBackup;
        settings.anaglyph = anaglyphBackup;
        mc.renderGlobal = renderBackup;
        mc.displayWidth = widthBackup;
        mc.displayHeight = heightBackup;
        entity.setDead();
    }

    @Override
    public void render(TileEntityTelevision te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if (!te.isConnected()) {
            return;
        }
        TileEntityCamera cam = (TileEntityCamera) mc.world.getTileEntity(te.getChannel());
        if (cam == null) {
            return;
        }

        if (mc.player.getDistanceSq(te.getPos()) > 100) return;
        if (!channelsInView.contains(te.getChannel())) channelsInView.add(te.getChannel());

        EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockTelevision.FACING);

        GlStateManager.disableLighting();
        GlStateManager.translate(x, y, z);

        GlStateManager.translate(0.5, 0.5, 0.5);
        switch (facing) {
            case WEST:
                GlStateManager.rotate(0, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case SOUTH:
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            case NORTH:
                GlStateManager.rotate(270, 0, 1, 0);
                break;
            default:
                break;
        }
        GlStateManager.translate(-0.5, -0.5, -0.5);

        GlStateManager.bindTexture(cam.getTexture());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        bufferbuilder.pos(15 * px - 0.001, 1 - px + 0.01, px - 0.01).tex(0.0D, 1.0D).lightmap(240, 240).color(1, 1, 1, 1f).endVertex();
        bufferbuilder.pos(15 * px - 0.001, px - 0.01, px - 0.01).tex(0.0D, 0.0D).lightmap(240, 240).color(1, 1, 1, 1f).endVertex();
        bufferbuilder.pos(15 * px - 0.001, px - 0.01, 1 - px + 0.01).tex(1.0D, 0.0D).lightmap(240, 240).color(1, 1, 1, 1f).endVertex();
        bufferbuilder.pos(15 * px - 0.001, 1 - px + 0.01, 1 - px + 0.01).tex(1.0D, 1.0D).lightmap(240, 240).color(1, 1, 1, 1f).endVertex();
        tessellator.draw();

        GlStateManager.translate(0.5, 0.5, 0.5);
        switch (facing) {
            case WEST:
                GlStateManager.rotate(-0, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(-180, 0, 1, 0);
                break;
            case SOUTH:
                GlStateManager.rotate(-90, 0, 1, 0);
                break;
            case NORTH:
                GlStateManager.rotate(-270, 0, 1, 0);
                break;
            default:
                break;
        }
        GlStateManager.translate(-0.5, -0.5, -0.5);

        GlStateManager.translate(-x, -y, -z);
        GlStateManager.enableLighting();
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (mc.world != null) {
            for (BlockPos pos : channelsInView) {
                if (mc.world.getBlockState(pos).getBlock() == ContentLoader.camera) {
                    TileEntityCamera cam = (TileEntityCamera) mc.world.getTileEntity(pos);
                    EnumFacing f = mc.world.getBlockState(pos).getValue(BlockHorizontal.FACING);
                    renderWorld(cam.getFramebuffer(), pos, f, mc, event.renderTickTime);
                }
            }
            channelsInView.clear();
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        channelsInView.clear();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld().isRemote) {
            cameraRenderGlobal.setWorldAndLoadRenderers((WorldClient) event.getWorld());
        }
    }

    @SubscribeEvent
    public void onPrePlayerRender(RenderPlayerEvent.Pre event) {
        if (!rendering) return;

        if (event.getEntityPlayer() == renderEntity) {
            backupEntity = Minecraft.getMinecraft().getRenderManager().renderViewEntity;
            Minecraft.getMinecraft().getRenderManager().renderViewEntity = renderEntity;
        }
    }

    @SubscribeEvent
    public void onPostPlayerRender(RenderPlayerEvent.Post event) {
        if (!rendering) return;

        if (event.getEntityPlayer() == renderEntity) {
            Minecraft.getMinecraft().getRenderManager().renderViewEntity = backupEntity;
            renderEntity = null;
        }
    }

}

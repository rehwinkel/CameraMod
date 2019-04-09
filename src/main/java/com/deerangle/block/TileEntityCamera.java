package com.deerangle.block;

import com.deerangle.main.CameraWorldSavedData;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class TileEntityCamera extends TileEntity {

    private int texture;
    private int fbo;
    private int rbo;

    @Override
    public void onLoad() {
        CameraWorldSavedData data = CameraWorldSavedData.get(world);
        if (!world.isRemote) {
            if (!data.hasCamera(getPos())) {
                data.addCamera(getPos());
            }
        } else {
            fbo = GL30.glGenFramebuffers();
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

            rbo = GL30.glGenRenderbuffers();
            GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo);
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT32F, RenderTelevision.quality, RenderTelevision.quality);
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, rbo);

            texture = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, RenderTelevision.quality, RenderTelevision.quality, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }
    }

    @Override
    public void invalidate() {
        if (getWorld().isRemote) {
            GL11.glDeleteTextures(getTexture());
            GL30.glDeleteFramebuffers(getFramebuffer());
            GL30.glDeleteRenderbuffers(getRenderbuffer());
        }
        super.invalidate();
    }

    public int getTexture() {
        return texture;
    }

    public int getFramebuffer() {
        return fbo;
    }

    public int getRenderbuffer() {
        return rbo;
    }

}

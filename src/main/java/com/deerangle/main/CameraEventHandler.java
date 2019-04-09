package com.deerangle.main;

import com.deerangle.block.BlockCamera;
import com.google.common.collect.ImmutableSetMultimap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Mod.EventBusSubscriber
public class CameraEventHandler {

    /*private Field pcm;
    private Method getOrCreate;

    public CameraEventHandler() {
        try {
            pcm = WorldServer.class.getDeclaredField("playerChunkMap");
            pcm.setAccessible(true);
            getOrCreate = PlayerChunkMap.class.getDeclaredMethod("getOrCreateEntry", int.class, int.class);
            getOrCreate.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) throws InvocationTargetException, IllegalAccessException {
        if (!event.getWorld().isRemote) {
            BlockCamera.ticket = null;
            //System.out.println("WORLD chunk!");
            //forceReloadChunk((WorldServer) event.getWorld(), 0, 0, true);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) throws InvocationTargetException, IllegalAccessException {
        //System.out.println("LOAD WORD");
        if (!event.getWorld().isRemote) {
            //BlockCamera.ticket = ForgeChunkManager.requestTicket(CameraMod.INSTANCE, event.getWorld(), ForgeChunkManager.Type.NORMAL);
            //forceReloadChunk((WorldServer) event.getWorld(), 0, 0, true);
        }
    }

    @SubscribeEvent
    public void onChunkWatch(ChunkWatchEvent.UnWatch event) throws IllegalAccessException, InvocationTargetException {
        //forceReloadChunk((WorldServer) event.getChunkInstance().getWorld(), event.getChunk().x, event.getChunk().z, false);
    }

    public void forceReloadChunk(WorldServer world, int x, int z, boolean ignoreCoords) throws IllegalAccessException, InvocationTargetException {
        PlayerChunkMap map = (PlayerChunkMap) pcm.get(world);

        List<EntityPlayerMP> players = world.getPlayers(EntityPlayerMP.class, (p) -> true);
        ImmutableSetMultimap<ChunkPos, ForgeChunkManager.Ticket> chunks = ForgeChunkManager.getPersistentChunksFor(world);

        for (ChunkPos pos : chunks.keySet()) {
            //System.out.println(pos);
            if (ignoreCoords || (z == pos.z && x == pos.x)) {
                PlayerChunkMapEntry e = (PlayerChunkMapEntry) getOrCreate.invoke(map, pos.x, pos.z);
                System.out.println("Reloaded chunk!");

                for (EntityPlayerMP player : players) {
                    if (e != null) {
                        e.sendToPlayer(player);
                    }
                }
            }
        }
    }

    private void increaseChunkCap() {
        Configuration cfg = ForgeChunkManager.getConfig();
        Property modTC = cfg.get(CameraMod.MODID, "maximumTicketCount", 1000);
        System.out.println(modTC.getInt());
        Property modCPT = cfg.get(CameraMod.MODID, "maximumChunksPerTicket", 2000);
        System.out.println(modCPT.getInt());
        cfg.save();
    }*/

}

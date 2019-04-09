package com.deerangle.network;

import com.deerangle.block.TileEntityCamera;
import com.deerangle.block.TileEntityTelevision;
import com.deerangle.camera.EntityCamera;
import com.deerangle.main.CameraMod;
import com.deerangle.main.ContentLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

    public static SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(CameraMod.MODID);

    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ContentLoader());
        NETWORK.registerMessage(UpdateTelevisionMessage.Handler.class, UpdateTelevisionMessage.class, 0, Side.CLIENT);
        NETWORK.registerMessage(RequestUpdateTelevisionMessage.Handler.class, RequestUpdateTelevisionMessage.class, 1, Side.SERVER);
    }

    public void onInit(FMLInitializationEvent event) {
        EntityRegistry.registerModEntity(new ResourceLocation(CameraMod.MODID, "camera"), EntityCamera.class, "camera", 1, CameraMod.INSTANCE, 80, 1, false);
        GameRegistry.registerTileEntity(TileEntityTelevision.class, new ResourceLocation(CameraMod.MODID, "television"));
        GameRegistry.registerTileEntity(TileEntityCamera.class, new ResourceLocation(CameraMod.MODID, "camera"));
    }
}

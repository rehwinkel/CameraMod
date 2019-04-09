package com.deerangle.main;

import com.deerangle.network.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CameraMod.MODID, name = CameraMod.NAME, version = CameraMod.VERSION)
public class CameraMod {
    public static final String MODID = "camera";
    public static final String NAME = "Camera";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.deerangle.network.ClientProxy", serverSide = "com.deerangle.network.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Object INSTANCE;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        proxy.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInit(event);
    }
}

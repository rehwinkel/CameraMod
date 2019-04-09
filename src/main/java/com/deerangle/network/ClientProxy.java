package com.deerangle.network;

import com.deerangle.block.RenderTelevision;
import com.deerangle.block.TileEntityTelevision;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        MinecraftForge.EVENT_BUS.register(new RenderTelevision());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTelevision.class, new RenderTelevision());
    }
}

package com.deerangle.main;

import com.deerangle.block.BlockCamera;
import com.deerangle.block.BlockTelevision;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class ContentLoader {

    public static Block television;
    public static Block camera;
    public static Item circuit;

    @SubscribeEvent
    public void registerBlockEvent(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        television = new BlockTelevision();
        camera = new BlockCamera();

        registry.registerAll(television, camera);
    }

    @SubscribeEvent
    public void registerItemEvent(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        ItemBlock televisionItem = new ItemBlock(television);
        televisionItem.setRegistryName(television.getRegistryName());

        ItemBlock cameraItem = new ItemBlock(camera);
        cameraItem.setRegistryName(camera.getRegistryName());
        circuit = new Item().setCreativeTab(CreativeTabs.REDSTONE).setRegistryName("circuit").setUnlocalizedName("circuit");

        registry.registerAll(televisionItem, cameraItem, circuit);
    }

    @SubscribeEvent
    public void registerModelEvent(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(television), 0, getModelResource("television"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(camera), 0, getModelResource("camera"));
        ModelLoader.setCustomModelResourceLocation(circuit, 0, getModelResource("circuit"));
    }

    private ModelResourceLocation getModelResource(String name) {
        return new ModelResourceLocation(new ResourceLocation(CameraMod.MODID, name), "inventory");
    }

}

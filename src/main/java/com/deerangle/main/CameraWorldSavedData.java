package com.deerangle.main;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class CameraWorldSavedData extends WorldSavedData {

    private static final String DATA_NAME = CameraMod.MODID + "_cameras";

    private ArrayList<BlockPos> cameraPositions = new ArrayList<>();

    public CameraWorldSavedData() {
        super(DATA_NAME);
    }

    public CameraWorldSavedData(String s) {
        super(s);
    }

    public static CameraWorldSavedData get(World world) {
        MapStorage storage = world.getMapStorage();
        CameraWorldSavedData data = (CameraWorldSavedData) storage.getOrLoadData(CameraWorldSavedData.class, DATA_NAME);

        if (data == null) {
            data = new CameraWorldSavedData();
            storage.setData(DATA_NAME, data);
        }
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        cameraPositions.clear();
        NBTTagList list = (NBTTagList) nbt.getTag("cameraPositions");
        for (NBTBase data : list) {
            BlockPos pos = BlockPos.fromLong(((NBTTagLong) data).getLong());
            cameraPositions.add(pos);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (BlockPos pos : cameraPositions) {
            list.appendTag(new NBTTagLong(pos.toLong()));
        }
        compound.setTag("cameraPositions", list);
        return compound;
    }

    public void addCamera(BlockPos pos) {
        cameraPositions.add(pos);
        markDirty();
    }

    public List<BlockPos> getCameras() {
        return cameraPositions;
    }

    public boolean hasCamera(BlockPos pos) {
        return cameraPositions.contains(pos);
    }

    public void removeCamera(BlockPos pos) {
        cameraPositions.remove(pos);
        markDirty();
    }
}

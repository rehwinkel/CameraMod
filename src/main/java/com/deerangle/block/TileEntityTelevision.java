package com.deerangle.block;

import com.deerangle.main.CameraWorldSavedData;
import com.deerangle.network.CommonProxy;
import com.deerangle.network.RequestUpdateTelevisionMessage;
import com.deerangle.network.UpdateTelevisionMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityTelevision extends TileEntity implements ITickable {

    private BlockPos currentChannel;
    private CameraWorldSavedData saveData;

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            saveData = CameraWorldSavedData.get(world);
        } else {
            CommonProxy.NETWORK.sendToServer(new RequestUpdateTelevisionMessage(getPos()));
        }
    }

    public void switchChannel() {
        if (currentChannel == null) {
            if (saveData.getCameras().size() > 0) {
                updateChannel(saveData.getCameras().get(0));
            }
        } else {
            List<BlockPos> cameras = saveData.getCameras();
            int currentIndex = cameras.indexOf(currentChannel);
            if (currentIndex == cameras.size() - 1) {
                updateChannel(null);
                return;
            } else {
                currentIndex++;
            }
            updateChannel(cameras.get(currentIndex));
        }
    }

    public void updateChannel(BlockPos pos) {
        currentChannel = pos;
        if (!world.isRemote) {
            CommonProxy.NETWORK.sendToAll(new UpdateTelevisionMessage(getPos(), pos));
        }
    }

    @Override
    public void update() {
        //if television has a channel that doesn't exist, remove the channel
        if (!world.isRemote) {
            if (currentChannel != null) {
                if (!saveData.getCameras().contains(currentChannel)) {
                    updateChannel(null);
                }
            }
        }
    }

    public boolean isConnected() {
        return currentChannel != null;
    }

    public BlockPos getChannel() {
        return currentChannel;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (currentChannel != null) {
            compound.setLong("channel", currentChannel.toLong());
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("channel")) {
            currentChannel = BlockPos.fromLong(compound.getLong("channel"));
        }
    }
}

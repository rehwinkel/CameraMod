package com.deerangle.camera;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCamera extends Entity {

    public EntityCamera(World worldIn) {
        super(worldIn);
        setSize(0.1f, 0.1f);
        this.noClip = true;
    }

    public EntityCamera(World worldIn, BlockPos pos, EnumFacing facing) {
        super(worldIn);
        this.noClip = true;

        setRotation(getYaw(facing), 0);
        double offx = 0, offz = 0;
        if (facing == EnumFacing.WEST) {
            offz = 0.5;
        } else if (facing == EnumFacing.EAST) {
            offz = 0.5;
            offx = 1;
        } else if (facing == EnumFacing.SOUTH) {
            offx = 0.5;
            offz = 1;
        } else if (facing == EnumFacing.NORTH) {
            offx = 0.5;
        }
        setPosition(pos.getX() + offx, pos.getY(), pos.getZ() + offz);
    }

    private float getYaw(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return 180;
            case SOUTH:
                return 0;
            case EAST:
                return 270;
            case WEST:
                return 90;
            default:
                return 0;
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        this.setDead();
    }

}

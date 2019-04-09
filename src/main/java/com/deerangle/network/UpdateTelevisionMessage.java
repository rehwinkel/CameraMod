package com.deerangle.network;

import com.deerangle.block.TileEntityTelevision;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateTelevisionMessage implements IMessage {

    private BlockPos tePos;
    private BlockPos channel;

    public UpdateTelevisionMessage() {
    }

    public UpdateTelevisionMessage(BlockPos tePos, BlockPos channel) {
        this.tePos = tePos;
        this.channel = channel;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tePos = BlockPos.fromLong(buf.readLong());
        boolean isNull = buf.readBoolean();
        if (!isNull) {
            channel = BlockPos.fromLong(buf.readLong());
        } else {
            channel = null;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tePos.toLong());
        boolean isNull = channel == null;
        buf.writeBoolean(isNull);
        if (!isNull) {
            buf.writeLong(channel.toLong());
        }
    }

    public static class Handler implements IMessageHandler<UpdateTelevisionMessage, IMessage> {

        @Override
        public IMessage onMessage(UpdateTelevisionMessage message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                TileEntityTelevision tv = (TileEntityTelevision) mc.world.getTileEntity(message.tePos);
                if (tv != null) {
                    tv.updateChannel(message.channel);
                }
            });
            return null;
        }

    }
}

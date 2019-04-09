package com.deerangle.network;

import com.deerangle.block.TileEntityTelevision;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestUpdateTelevisionMessage implements IMessage {

    private BlockPos tePos;

    public RequestUpdateTelevisionMessage() {
    }

    public RequestUpdateTelevisionMessage(BlockPos tePos) {
        this.tePos = tePos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tePos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tePos.toLong());
    }

    public static class Handler implements IMessageHandler<RequestUpdateTelevisionMessage, UpdateTelevisionMessage> {

        @Override
        public UpdateTelevisionMessage onMessage(RequestUpdateTelevisionMessage message, MessageContext ctx) {
            TileEntityTelevision tv = (TileEntityTelevision) ctx.getServerHandler().player.world.getTileEntity(message.tePos);
            if (tv != null) {
                return new UpdateTelevisionMessage(message.tePos, tv.getChannel());
            }
            return null;
        }

    }

}

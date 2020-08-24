package nl.hpfxd.limbo.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nl.hpfxd.limbo.network.protocol.PacketUtils;
import nl.hpfxd.limbo.network.packet.Packet;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        try {
            PacketUtils.writeVarInt(out, packet.getPacketId());
            packet.write(out);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

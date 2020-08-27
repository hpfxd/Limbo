package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;

public class PacketChatMessage extends Packet
{
    private byte type = 1;
    private String message;

    public PacketChatMessage(String message, byte type)
    {
        super(0x02);
        this.message = message;
        this.type = type;
    }

    @Override
    public void encode(ByteBuf buf)
    {
        this.writeString(buf, message);
        buf.writeByte(type);
    }

    @Override
    public void decode(ByteBuf byteBuf)
    {

    }
}

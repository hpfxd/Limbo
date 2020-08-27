package nl.hpfxd.limbo.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.Getter;
import lombok.Setter;

/*
 * packet class is only used for outgoing packets
 * i'm too tired to come up with a way for incoming packets without using reflection
 */
public class Packet
{
    @Getter protected int packetId; // updated by the write method if changed in another version
    @Setter protected int protocolVersion = 47;

    protected Packet(int packetId)
    {
        this.packetId = packetId;
    }

    public void writeString(ByteBuf buf, CharSequence str)
    {
        int size = ByteBufUtil.utf8Bytes(str);
        writeVarInt(buf, size);
        ByteBufUtil.writeUtf8(buf, str);
    }

    public void writeVarInt(ByteBuf buf, int value) {
        while (true) {
            if ((value & 0xFFFFFF80) == 0) {
                buf.writeByte(value);
                return;
            }

            buf.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
    }

    public void encode(ByteBuf buf)
    {

    }

    public void decode(ByteBuf byteBuf)
    {

    }
}

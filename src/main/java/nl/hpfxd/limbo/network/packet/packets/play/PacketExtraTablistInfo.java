package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;

public class PacketExtraTablistInfo extends Packet
{
    private String header;
    private String footer;

    public PacketExtraTablistInfo(String header, String footer)
    {
        super(0x47);
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void encode(ByteBuf buf)
    {
        this.writeString(buf, header);
        this.writeString(buf, footer);
    }
}

package nl.hpfxd.limbo.network.packet.packets.status;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;

public class PacketServerListPong extends Packet {
    private final long id;

    public PacketServerListPong(long id) {
        super(0x01);
        this.id = id;
    }
    @Override
    public void encode(ByteBuf buf) {
        buf.writeLong(id);
    }
}

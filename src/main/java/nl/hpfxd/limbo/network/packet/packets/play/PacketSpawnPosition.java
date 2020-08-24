package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;

public class PacketSpawnPosition extends Packet {
    public PacketSpawnPosition() {
        super(0x05);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(0);
    }
}

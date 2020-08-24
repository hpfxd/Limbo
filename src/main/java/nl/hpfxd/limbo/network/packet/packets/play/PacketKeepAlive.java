package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.PacketUtils;

public class PacketKeepAlive extends Packet {
    private final int id;

    public PacketKeepAlive(int id) {
        super(0x00);
        this.id = id;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.id);
    }
}

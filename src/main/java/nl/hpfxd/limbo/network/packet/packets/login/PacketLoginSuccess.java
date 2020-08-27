package nl.hpfxd.limbo.network.packet.packets.login;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.protocol.PacketUtils;
import nl.hpfxd.limbo.network.packet.Packet;

import java.util.UUID;

public class PacketLoginSuccess extends Packet {
    private final UUID uniqueId;
    private final String name;

    public PacketLoginSuccess(UUID uniqueId, String name) {
        super(0x02);
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketUtils.writeString(buf, this.uniqueId.toString());
        PacketUtils.writeString(buf, this.name);
    }
}

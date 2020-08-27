package nl.hpfxd.limbo.network.packet.packets.login;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.PacketUtils;

public class PacketLoginDisconnect extends Packet {
    private final String reason;

    public PacketLoginDisconnect(String reason) {
        super(0x00);
        this.reason = reason;
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketUtils.writeString(buf, this.reason);
    }
}

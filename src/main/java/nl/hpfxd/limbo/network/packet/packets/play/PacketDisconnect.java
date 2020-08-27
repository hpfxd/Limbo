package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.PacketUtils;
import nl.hpfxd.limbo.network.protocol.ProtocolVersion;

public class PacketDisconnect extends Packet {
    private final String reason;

    public PacketDisconnect(String reason) {
        super(0x40);
        this.reason = reason;
    }

    @Override
    public void encode(ByteBuf buf) {
        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_12_2) {
            this.packetId = 0x1A;
        }

        PacketUtils.writeString(buf, this.reason);
    }
}

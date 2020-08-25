package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.PacketUtils;
import nl.hpfxd.limbo.network.protocol.ProtocolVersion;

public class PacketKeepAlive extends Packet {
    private final int id;

    public PacketKeepAlive(int id) {
        super(0x00);
        this.id = id;
    }

    @Override
    public void write(ByteBuf buf) {
        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_12_2) {
            this.packetId = 0x1F;
        }

        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_12_2) {
            buf.writeLong(this.id);
        } else if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_8) {
            PacketUtils.writeVarInt(buf, this.id);
        } else if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_7_10) {
            buf.writeInt(this.id);
        }
    }
}

package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.ProtocolVersion;

public class PacketSpawnPosition extends Packet {
    public PacketSpawnPosition() {
        super(0x05);
    }

    @Override
    public void encode(ByteBuf buf) {
        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_12_2) {
            this.packetId = 0x46;
        }

        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_8) {
            buf.writeLong(0);
        } else {
            buf.writeInt(0);
            buf.writeInt(0);
            buf.writeInt(0);
        }
    }
}

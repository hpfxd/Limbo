package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.ChatMessagePosition;
import nl.hpfxd.limbo.network.protocol.ProtocolVersion;

public class PacketChatMessage extends Packet {
    private final String message;
    private final ChatMessagePosition position;

    public PacketChatMessage(String message, ChatMessagePosition position) {
        super(0x02);
        this.message = message;
        this.position = position;
    }

    @Override
    public void encode(ByteBuf buf) {
        this.writeString(buf, this.message);

        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_8) {
            buf.writeByte(this.position.getId());
        }
    }

    @Override
    public void decode(ByteBuf byteBuf) {
    }
}

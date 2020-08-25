package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.PacketUtils;
import nl.hpfxd.limbo.network.protocol.ProtocolVersion;

public class PacketJoinGame extends Packet {
    private final int entityId;
    private final int gamemode;
    private final int dimension;
    private final int difficulty;
    private final int maxPlayers;
    private final String levelType;
    private final boolean reducedDebugInfo;

    public PacketJoinGame(int entityId, int gamemode, int dimension, int difficulty, int maxPlayers, String levelType, boolean reducedDebugInfo) {
        super(0x01);
        this.entityId = entityId;
        this.gamemode = gamemode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.reducedDebugInfo = reducedDebugInfo;
    }
    @Override
    public void write(ByteBuf buf) {
        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_12_2) {
            this.packetId = 0x23;
        }

        buf.writeInt(this.entityId);
        buf.writeByte(this.gamemode);

        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_12_2) {
            buf.writeInt(this.dimension);
        } else {
            buf.writeByte(this.dimension);
        }

        buf.writeByte(this.difficulty);
        buf.writeByte(this.maxPlayers);
        PacketUtils.writeString(buf, this.levelType);
        if (this.protocolVersion >= ProtocolVersion.PROTOCOL_1_8) buf.writeBoolean(this.reducedDebugInfo);
    }
}

package nl.hpfxd.limbo.player;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.packet.packets.login.PacketLoginSuccess;
import nl.hpfxd.limbo.network.packet.packets.play.PacketJoinGame;
import nl.hpfxd.limbo.network.packet.packets.play.PacketKeepAlive;
import nl.hpfxd.limbo.network.packet.packets.play.PacketPositionAndLook;
import nl.hpfxd.limbo.network.packet.packets.play.PacketSpawnPosition;
import nl.hpfxd.limbo.network.protocol.PacketUtils;
import nl.hpfxd.limbo.network.protocol.ProtocolState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Player {
    @Getter private final Channel channel;
    @Getter @Setter private ProtocolState state = ProtocolState.HANDSHAKE;
    @Getter private int version = 47;

    @Getter private String handshakeAddress;
    @Getter private int handshakePort;

    @Getter private String name;
    @Getter private UUID uniqueId;

    private ScheduledFuture<?> keepAliveFuture;

    public Player(Channel channel) {
        this.channel = channel;
    }

    public void destroy() {
        if (this.keepAliveFuture != null) {
            this.keepAliveFuture.cancel(false);
            this.keepAliveFuture = null;
        }
    }

    public void onPacket(ByteBuf buf) throws Exception {
        int id = PacketUtils.readVarInt(buf);

        if (this.state == ProtocolState.HANDSHAKE) {
            if (id == 0x00) { // handshake
                this.version = PacketUtils.readVarInt(buf);
                this.handshakeAddress = PacketUtils.readString(buf); // todo bungee ip forwarding
                this.handshakePort = buf.readUnsignedShort();

                int nextState = PacketUtils.readVarInt(buf);
                if (nextState == 1) {
                    this.state = ProtocolState.STATUS;
                } else if (nextState == 2) {
                    this.state = ProtocolState.LOGIN;
                } else {
                    throw new IOException("Next state should be 1 or 2, instead got: " + nextState);
                }
            }
        } else if (this.state == ProtocolState.LOGIN) {
            if (id == 0x00) { // login start
                this.name = PacketUtils.readString(buf);
                if (this.uniqueId == null) this.uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.name).getBytes(StandardCharsets.UTF_8));

                this.sendPacket(new PacketLoginSuccess(this.uniqueId, this.name));

                this.state = ProtocolState.PLAY;

                this.sendPacket(new PacketJoinGame(0, 1, (byte) 1, 0, 1, "flat", false));
                this.sendPacket(new PacketSpawnPosition());
                this.sendPacket(new PacketPositionAndLook(0, 0, 0, 0, 0));

                this.keepAliveFuture = channel.eventLoop().scheduleAtFixedRate(() -> this.sendPacket(new PacketKeepAlive((int) (System.currentTimeMillis() / 10000))), 5, 10, TimeUnit.SECONDS);
            }
        }
    }

    public void sendPacket(Packet packet) {
        this.channel.writeAndFlush(packet);
    }
}

package nl.hpfxd.limbo.player;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import nl.hpfxd.limbo.Limbo;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.packet.packets.login.PacketLoginDisconnect;
import nl.hpfxd.limbo.network.packet.packets.login.PacketLoginSuccess;
import nl.hpfxd.limbo.network.packet.packets.play.*;
import nl.hpfxd.limbo.network.packet.packets.status.PacketServerListPingResponse;
import nl.hpfxd.limbo.network.packet.packets.status.PacketServerListPong;
import nl.hpfxd.limbo.network.protocol.PacketUtils;
import nl.hpfxd.limbo.network.protocol.ProtocolState;
import nl.hpfxd.limbo.network.protocol.ProtocolVersion;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Log
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
            } else {
                this.invalidPacket();
            }
        } else if (this.state == ProtocolState.LOGIN) {
            if (id == 0x00) { // login start
                if (!ProtocolVersion.supportedVersions.contains(this.version)) {
                    this.disconnect(new JSONObject()
                            .put("text", "You are using an unsupported Minecraft version.")
                            .put("color", "RED"));
                    return;
                }

                this.name = PacketUtils.readString(buf);
                if (this.uniqueId == null) this.uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.name).getBytes(StandardCharsets.UTF_8));

                this.sendPacket(new PacketLoginSuccess(this.uniqueId, this.name));

                this.state = ProtocolState.PLAY;

                this.sendPacket(new PacketJoinGame(0, 1, 0, 0, 1, "flat", false));
                this.sendPacket(new PacketSpawnPosition());
                this.sendPacket(new PacketPositionAndLook(0, 100, 0, 0, 0));
                log.info("Player " + this.name + " joined the game.");

                this.keepAliveFuture = channel.eventLoop().scheduleAtFixedRate(() -> this.sendPacket(new PacketKeepAlive((int) (System.currentTimeMillis() / 10000))), 5, 10, TimeUnit.SECONDS);
            } else {
                this.invalidPacket();
            }
        } else if (this.state == ProtocolState.STATUS) {
            if (id == 0x00) { // ping request
                log.info("Player pinging.");
                JSONObject json = new JSONObject()
                        .put("players", new JSONObject()
                                .put("max", Limbo.getInstance().getMaxPlayers())
                                .put("online", Limbo.getInstance().getNetworkManager().getPlayers().size())) // this number includes all connections, not just players online
                        .put("description", Limbo.getInstance().getMotd());

                JSONObject version = new JSONObject()
                        .put("name", "github.com/hpfxd/Limbo | Minecraft 1.8.x");
                if (ProtocolVersion.supportedVersions.contains(this.version)) {
                    version.put("protocol", this.version);
                } else {
                    version.put("protocol", ProtocolVersion.PROTOCOL_1_8);
                }

                json.put("version", version);
                this.sendPacket(new PacketServerListPingResponse(json.toString()));
            } else if (id == 0x01) { // ping (used for displaying latency)
                long l = buf.readLong();

                this.sendPacket(new PacketServerListPong(l));
                this.disconnect(null);
            } else {
                this.invalidPacket();
            }
        }
    }

    private void invalidPacket() {
        this.disconnect(new JSONObject()
                .put("text", "Invalid packet data received.")
                .put("color", "RED"));
    }

    public void sendPacket(Packet packet) {
        packet.setProtocolVersion(this.version);
        this.channel.writeAndFlush(packet);
    }

    public void disconnect(JSONObject reason) {
        if (reason != null) {
            if (this.state == ProtocolState.PLAY) {
                this.sendPacket(new PacketDisconnect(reason.toString()));
            } else if (this.state == ProtocolState.LOGIN) {
                this.sendPacket(new PacketLoginDisconnect(reason.toString()));
            }
        }

        this.channel.close();
    }
}

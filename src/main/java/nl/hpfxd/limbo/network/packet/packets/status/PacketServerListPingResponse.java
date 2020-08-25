package nl.hpfxd.limbo.network.packet.packets.status;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;
import nl.hpfxd.limbo.network.protocol.PacketUtils;

public class PacketServerListPingResponse extends Packet {
    private final String json;

    public PacketServerListPingResponse(String json) {
        super(0x00);
        this.json = json;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeString(buf, json);
    }
}

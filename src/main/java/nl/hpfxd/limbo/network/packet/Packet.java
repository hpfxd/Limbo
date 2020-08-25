package nl.hpfxd.limbo.network.packet;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/*
 * packet class is only used for outgoing packets
 * i'm too tired to come up with a way for incoming packets without using reflection
 */
public abstract class Packet {
    @Getter protected int packetId; // updated by the write method if changed in another version
    @Setter protected int protocolVersion = 47;

    protected Packet(int packetId) {
        this.packetId = packetId;
    }

    public abstract void write(ByteBuf buf);
}

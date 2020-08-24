package nl.hpfxd.limbo.network.packet;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

/*
 * packet class is only used for outgoing packets
 * i'm too tired to come up with a way for incoming packets without using reflection
 */
public abstract class Packet {
    @Getter private final int packetId;

    protected Packet(int packetId) {
        this.packetId = packetId;
    }

    public abstract void write(ByteBuf buf);
}

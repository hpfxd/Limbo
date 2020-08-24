package nl.hpfxd.limbo.network.packet.packets.play;

import io.netty.buffer.ByteBuf;
import nl.hpfxd.limbo.network.packet.Packet;

public class PacketPositionAndLook extends Packet {
    private final int x;
    private final int y;
    private final int z;
    private final float yaw;
    private final float pitch;

    public PacketPositionAndLook(int x, int y, int z, float yaw, float pitch) {
        super(0x08);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeByte(0);
    }
}

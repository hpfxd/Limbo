package nl.hpfxd.limbo.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.extern.java.Log;
import nl.hpfxd.limbo.Limbo;
import nl.hpfxd.limbo.player.Player;

import java.util.logging.Level;

@Log
public class ChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Getter private Player player;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.fine("Player connected.");
        this.player = new Player(ctx.channel());
        Limbo.getInstance().getNetworkManager().getPlayers().add(this.player);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        dispatchSession();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        this.player.onPacket(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        dispatchSession();
        log.log(Level.WARNING, "Player error.", cause);
    }

    private void dispatchSession() {
        if (this.player == null) return;
        log.fine("Player disconnected.");
        Limbo.getInstance().getNetworkManager().getPlayers().remove(this.player);
        this.player.destroy();
        this.player = null;
    }
}

package nl.hpfxd.limbo.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.extern.java.Log;
import nl.hpfxd.limbo.player.Player;

@Log
public class ChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Getter private Player player;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Player connected.");
        NetworkManager.getPlayers().add(this.player);
        this.player = new Player(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        dispatchSession();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        this.player.onPacket(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        dispatchSession();
        log.severe("Player error.");
        cause.printStackTrace();
    }

    private void dispatchSession() {
        if (this.player == null) return;
        log.info("Player disconnected.");
        NetworkManager.getPlayers().remove(this.player);
        this.player.destroy();
        this.player = null;
    }
}

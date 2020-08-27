package nl.hpfxd.limbo.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.extern.java.Log;
import nl.hpfxd.limbo.Limbo;
import nl.hpfxd.limbo.network.pipeline.PacketEncoder;
import nl.hpfxd.limbo.network.pipeline.VarInt21Decoder;
import nl.hpfxd.limbo.network.pipeline.VarInt21Encoder;
import nl.hpfxd.limbo.player.Player;

import java.util.ArrayList;
import java.util.List;

@Log
public class NetworkManager {
    @Getter private final List<Player> players = new ArrayList<>();
    private EventLoopGroup bossgroup;

    public void start() {
        log.info("Starting network manager.");

        if (Epoll.isAvailable()) {
            log.info("Using Epoll transport.");
        } else {
            log.info("Epoll not available, falling back to NIO. Reason: " + Epoll.unavailabilityCause().getMessage());
        }
        this.bossgroup = this.getEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(this.bossgroup)
                .channel(this.getChannel())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("timer", new ReadTimeoutHandler(30))

                                .addLast("varintdecoder", new VarInt21Decoder())

                                .addLast("varintencoder", new VarInt21Encoder())
                                .addLast("packetencoder", new PacketEncoder())

                                .addLast("handler", new ChannelHandler());
                    }
                });

        try {
            Channel ch = bootstrap.bind(Limbo.getInstance().getConfig().getProperty("network.address"), Limbo.getInstance().getPort()).sync().channel();
            log.info("Listening for connections on " + ch.localAddress().toString() + ".");
            ch.closeFuture().sync();
        } catch (Exception e) {
            log.severe("Failed to bind to port!");
            log.severe("Make sure that no other servers are using the port in server.properties.");
            log.severe("Exception: " + e.getMessage());
            System.exit(1);
        }
    }

    public void shutdown() {
        this.bossgroup.shutdownGracefully();
    }

    private EventLoopGroup getEventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    private Class<? extends ServerChannel> getChannel() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}

package io.github.escapehonbab.netty;

import io.github.escapehonbab.jpa.objects.DesiredTarget;
import io.github.escapehonbab.jpa.objects.User;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Arrays;

public class MatchingServerBootstrap {

    private static MatchingServerBootstrap instance;
    private final int port;
    private final String host;

    private MatchingServerBootstrap(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public static MatchingServerBootstrap getInstance(String host, int port) {
        if (instance == null)
            return (instance = new MatchingServerBootstrap(host, port));

        return instance;
    }

    public void startServer() {
        MatchingServerHandler handler = new MatchingServerHandler();
        EventLoopGroup parentGroup = new NioEventLoopGroup(3);
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.AUTO_READ, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new IdleStateHandler(60, 30, 0));
                            socketChannel.pipeline().addLast(
                                    MessageDecoder.builder().registeredClasses(Arrays.asList(DesiredTarget.class, User.class)).build(),
                                    new MessageEncoder(),
                                    handler);
                        }
                    });
            ChannelFuture future = bootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                childGroup.shutdownGracefully().sync();
                parentGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

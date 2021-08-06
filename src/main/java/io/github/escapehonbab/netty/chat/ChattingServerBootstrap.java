package io.github.escapehonbab.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class ChattingServerBootstrap {
    private final int port;
    private final String host;

    public ChattingServerBootstrap(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() {
        ChattingServerHandler handler = new ChattingServerHandler();
        EventLoopGroup parentGroup = new NioEventLoopGroup(3);
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 30, 0));
                            socketChannel.pipeline().addLast("deadConnectionHandler", new DeadConnectionHandler());
                            socketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                                            ClassResolvers.weakCachingConcurrentResolver(getClass().getClassLoader())),
                                    new ObjectEncoder());
                            socketChannel.pipeline().addLast(handler);
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

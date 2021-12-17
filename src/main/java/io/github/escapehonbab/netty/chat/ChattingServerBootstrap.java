package io.github.escapehonbab.netty.chat;

import io.github.escapehonbab.jpa.objects.MessageBundle;
import io.github.escapehonbab.netty.MessageDecoder;
import io.github.escapehonbab.netty.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
@Component
public class ChattingServerBootstrap {
    private final ChattingServerHandler handler;
    private final ChattingPool pool;
    private final DeadConnectionHandler connectionHandler;
    @Value("${chatting.server.port}")
    private int port;
    @Value("${server.host}")
    private String host;

    public ChattingServerBootstrap(ChattingServerHandler handler, ChattingPool pool, DeadConnectionHandler deadConnectionHandler) {
        this.handler = handler;
        this.pool = pool;
        this.connectionHandler = deadConnectionHandler;
    }

    @PostConstruct
    public void startServer() {
        Thread t = new Thread(() -> {
            ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
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
                                socketChannel.pipeline().addLast(MessageDecoder.builder().registeredClasses(Arrays.asList(MessageBundle.class)).build(),
                                        new MessageEncoder());
                                socketChannel.pipeline().addLast(handler);
                                group.add(socketChannel);
                            }
                        });
                Channel ch = bootstrap.bind(host, port).sync().channel();
                ch.closeFuture().sync();
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
        });
        t.start();
    }
}

package io.github.escapehonbab.netty;

import io.github.escapehonbab.jpa.objects.DesiredTarget;
import io.github.escapehonbab.jpa.objects.User;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
@Component
public class MatchingServerBootstrap {


    @Value("${matching.server.port}")
    private int port;

    @Value("${server.host}")
    private String host;

    private final MatchingServerHandler handler;

    public MatchingServerBootstrap(MatchingServerHandler handler) {
        this.handler = handler;
    }

    @PostConstruct
    public void startServer() {
        Thread t = new Thread(() -> {
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

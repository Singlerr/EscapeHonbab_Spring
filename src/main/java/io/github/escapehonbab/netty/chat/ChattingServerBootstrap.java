package io.github.escapehonbab.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Component
public class ChattingServerBootstrap {
    @Value("${chatting.server.port}")
    private int port;

    @Value("${server.host}")
    private String host;

    private ChattingServerHandler handler;

    private ChattingPool pool;

    private DeadConnectionHandler connectionHandler;

    public ChattingServerBootstrap(ChattingServerHandler handler,ChattingPool pool,DeadConnectionHandler deadConnectionHandler){
        this.handler = handler;
        this.pool = pool;
        this.connectionHandler = deadConnectionHandler;
    }

    @PostConstruct
    public void startServer() {
        Thread t = new Thread(() -> {
            ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            //ChattingPool pool = ChattingPool.getInstance(group);
            //ChattingServerHandler handler = new ChattingServerHandler(pool);
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
                                socketChannel.pipeline().addLast("deadConnectionHandler",connectionHandler);
                                socketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                                                ClassResolvers.weakCachingConcurrentResolver(getClass().getClassLoader())),
                                        new ObjectEncoder());
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

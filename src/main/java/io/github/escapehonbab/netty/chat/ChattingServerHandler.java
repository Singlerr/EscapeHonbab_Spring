package io.github.escapehonbab.netty.chat;


import io.github.escapehonbab.jpa.objects.MessageBundle;
import io.github.escapehonbab.spring.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class ChattingServerHandler extends ChannelInboundHandlerAdapter {

    private final UserService service;

    private final ChattingPool pool;

    public ChattingServerHandler(UserService service, ChattingPool pool) {
        this.service = service;
        this.pool = pool;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageBundle) {
            MessageBundle bundle = (MessageBundle) msg;
            if (bundle.getState().equals("RECEIVE")) {
                ctx.writeAndFlush(bundle);
            } else if (bundle.getState().equals("SEND")) {
                assert bundle.getTargetUserId() != null;
                if (pool.isInConnection(bundle.getTargetUserId())) {
                    Channel target = pool.getConnection(bundle.getTargetUserId());
                    bundle.setState("RECEIVE");
                    target.writeAndFlush(bundle);
                }
            }
        }
    }
}

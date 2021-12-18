package io.github.escapehonbab.netty.chat;


import io.github.escapehonbab.jpa.objects.MessageBundle;
import io.github.escapehonbab.spring.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@ChannelHandler.Sharable
@Component
public class ChattingServerHandler extends ChannelInboundHandlerAdapter {

    private final UserService service;

    private final ChattingPool pool;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageBundle) {
            MessageBundle bundle = (MessageBundle) msg;
            if (bundle.getState().equalsIgnoreCase("HANDSHAKE")) {
                pool.registerConnection(bundle.getOwnerId(), ctx.channel());
                return;
            }
            assert bundle.getTargetUserId() != null;
            if (pool.isInConnection(bundle.getTargetUserId())) {
                Channel target = pool.getConnection(bundle.getTargetUserId());
                target.writeAndFlush(bundle);
            }
        }
    }
}

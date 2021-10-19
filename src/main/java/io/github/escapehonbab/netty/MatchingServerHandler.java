package io.github.escapehonbab.netty;

import io.github.escapehonbab.UserMatchingState;
import io.github.escapehonbab.controller.objects.ResponseBundle;
import io.github.escapehonbab.handler.MatchingHandler;
import io.github.escapehonbab.handler.MatchingUserWrapper;
import io.github.escapehonbab.jpa.DatabaseHandler;
import io.github.escapehonbab.jpa.objects.DesiredTarget;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Optional;

@ChannelHandler.Sharable
public class MatchingServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //No connection
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush("ping");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        if (!(o instanceof DesiredTarget)) {
            ctx.writeAndFlush(ResponseBundle.builder().response(StaticMessage.UNSUPPORTED_DATA_TYPE).responseCode(400).build());
            return;
        }
        DesiredTarget target = (DesiredTarget) o;

        Optional<User> opt = DatabaseHandler.getInstance().getDatabase().find(User.class).where().eq("id", target.getUser().getUserId()).findOneOrEmpty();
        if (!opt.isEmpty()) {
            MatchingUserWrapper wrapper = MatchingUserWrapper.getInstance(target, (userWrapper, target1) -> {
                userWrapper.setState(UserMatchingState.MATCHED);
                ctx.writeAndFlush(target1);
            });
            MatchingHandler.getInstance().submit(wrapper);
        } else {
            ctx.writeAndFlush(ResponseBundle.builder().response(StaticMessage.ERROR_NO_USER_FOUND).responseCode(400).build());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

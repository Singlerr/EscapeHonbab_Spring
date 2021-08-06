package io.github.escapehonbab.netty;

import io.github.escapehonbab.UserMatchingState;
import io.github.escapehonbab.controller.objects.ResponseBundle;
import io.github.escapehonbab.handler.MatchingHandler;
import io.github.escapehonbab.handler.MatchingHandlerCallback;
import io.github.escapehonbab.handler.MatchingUserWrapper;
import io.github.escapehonbab.jpa.DatabaseHandler;
import io.github.escapehonbab.jpa.objects.GPSData;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class MatchingServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof GPSData)) {
            ctx.writeAndFlush(ResponseBundle.builder().response(StaticMessage.UNSUPPORTED_DATA_TYPE).responseCode(400));
            return;
        }
        GPSData data = (GPSData) msg;
        User user = DatabaseHandler.getInstance().getDatabase().find(User.class).where().eq("id", data.getId()).findOne();
        if (user != null) {
            user.setGpsData(data);
            MatchingUserWrapper wrapper = MatchingUserWrapper.getInstance(user, new MatchingHandlerCallback() {
                @Override
                public void onMatched(MatchingUserWrapper userWrapper, User target) {
                    userWrapper.setState(UserMatchingState.MATCHED);
                    ctx.writeAndFlush(target);
                }
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

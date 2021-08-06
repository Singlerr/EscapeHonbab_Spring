package io.github.escapehonbab.netty.chat;


import io.ebean.Database;
import io.github.escapehonbab.jpa.DatabaseHandler;
import io.github.escapehonbab.jpa.objects.MessageBundle;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChattingServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageBundle) {
            MessageBundle bundle = (MessageBundle) msg;
            Database database = DatabaseHandler.getInstance().getDatabase();
            if (database.find(User.class).where().eq("userId", bundle.getTargetUserId()).exists()) {

            } else {
                bundle.setState(StaticMessage.ERROR_NO_USER_FOUND);
                ctx.writeAndFlush(bundle);
            }
        }
    }
}

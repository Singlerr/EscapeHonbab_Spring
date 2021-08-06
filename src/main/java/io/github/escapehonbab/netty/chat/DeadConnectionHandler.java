package io.github.escapehonbab.netty.chat;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.Serializable;

public class DeadConnectionHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ChattingPool.getInstance().removeConnection(ctx);
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new PingMessage());
            }
        }
    }

    private class PingMessage implements Serializable {
        public PingMessage() {
        }
    }
}

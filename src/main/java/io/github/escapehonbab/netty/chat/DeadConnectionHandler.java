package io.github.escapehonbab.netty.chat;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class DeadConnectionHandler extends ChannelDuplexHandler {

    private ChattingPool pool;

    public DeadConnectionHandler(ChattingPool pool){
        this.pool = pool;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                pool.closeConnection(ctx.channel().id());
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new PingMessage());
            }
        }
    }

    public static class PingMessage implements Serializable {
        public PingMessage() {
        }
    }
}

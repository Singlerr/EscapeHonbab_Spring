package io.github.escapehonbab.netty.chat;


import io.github.escapehonbab.jpa.objects.MessageBundle;
import io.github.escapehonbab.spring.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChattingServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private UserService service;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageBundle) {
            MessageBundle bundle = (MessageBundle) msg;
        }
    }
}

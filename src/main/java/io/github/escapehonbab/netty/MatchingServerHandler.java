package io.github.escapehonbab.netty;

import io.github.escapehonbab.UserMatchingState;
import io.github.escapehonbab.handler.MatchingHandler;
import io.github.escapehonbab.handler.MatchingUserWrapper;
import io.github.escapehonbab.jpa.objects.DesiredTarget;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import io.github.escapehonbab.netty.chat.DeadConnectionHandler;
import io.github.escapehonbab.spring.jwt.JWTAuthenticationTokenProvider;
import io.github.escapehonbab.spring.objects.ResponseBundle;
import io.github.escapehonbab.spring.service.UserService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ChannelHandler.Sharable
public class MatchingServerHandler extends ChannelInboundHandlerAdapter {
    private UserService service;
    private JWTAuthenticationTokenProvider provider;

    public MatchingServerHandler(JWTAuthenticationTokenProvider provider, UserService service) {
        this.service = service;
        this.provider = provider;
    }


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
                ctx.writeAndFlush(new DeadConnectionHandler.PingMessage());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {

        if (!(o instanceof DesiredTarget)) {
            ctx.writeAndFlush(ResponseBundle.builder().response(StaticMessage.UNSUPPORTED_DATA_TYPE).responseCode(HttpStatus.BAD_REQUEST.value()).build());
            return;
        }
        DesiredTarget target = (DesiredTarget) o;
        if (provider.validateToken(target.getUser().getPassword())) {
            Optional<User> opt = service.findByUserId(target.getUser().getUserId());
            if (opt.isPresent()) {
                target.setUser(opt.get());
                MatchingUserWrapper wrapper = MatchingUserWrapper.getInstance(target, (wrapper1, target1) -> {
                    wrapper1.setState(UserMatchingState.MATCHED);
                    ctx.writeAndFlush(target1);
                });
                MatchingHandler.getInstance().submit(wrapper);
                ctx.writeAndFlush(ResponseBundle.builder().response(HttpStatus.OK.getReasonPhrase()).responseCode(HttpStatus.OK.value()).build());
            } else {
                ctx.writeAndFlush(ResponseBundle.builder().response(StaticMessage.ERROR_NO_USER_FOUND).responseCode(HttpStatus.FORBIDDEN.value()).build());
            }
        } else {
            ctx.writeAndFlush(ResponseBundle.builder().response(StaticMessage.ERROR_TOKEN_VALIDATION_FAILED).responseCode(HttpStatus.BAD_REQUEST.value()).build());
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

package io.github.escapehonbab.netty;

import io.github.escapehonbab.UserMatchingState;
import io.github.escapehonbab.handler.MatchingHandler;
import io.github.escapehonbab.handler.MatchingUserWrapper;
import io.github.escapehonbab.jpa.objects.DesiredTarget;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import io.github.escapehonbab.spring.jwt.JWTAuthenticationTokenProvider;
import io.github.escapehonbab.spring.objects.ResponseBundle;
import io.github.escapehonbab.spring.service.TemporaryImageService;
import io.github.escapehonbab.spring.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@AllArgsConstructor
@Component
@ChannelHandler.Sharable
public class MatchingServerHandler extends ChannelInboundHandlerAdapter {
    private final UserService service;
    private final JWTAuthenticationTokenProvider provider;
    private final TemporaryImageService imageService;
    @Getter
    private ChannelGroup channels;

    /**
     * public MatchingServerHandler(JWTAuthenticationTokenProvider provider, UserService service,ChannelGroup channels, TemporaryImageService imageService) {
     * this.service = service;
     * this.provider = provider;
     * this.channels = channels;
     * this.imageService = imageService;
     * }
     **/

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
                ctx.writeAndFlush(new PingMessage());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Transactional
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
                target.setUser(opt.get().deepCopy());
                MatchingUserWrapper wrapper = MatchingUserWrapper.getInstance(target, (wrapper1, target1) -> {
                    wrapper1.setState(UserMatchingState.MATCHED);
                    ResponseBundle bundle = ResponseBundle.builder().build();
                    String hash = imageService.generateHash(target1.getImage());
                    User clone = target1.deepCopy();
                    clone.setImage(null);
                    clone.setResult(hash);
                    bundle.setMessage(clone);
                    bundle.setResponseCode(HttpStatus.OK.value());
                    if (ctx.channel().isActive()) {
                        Channel ch = channels.find(ctx.channel().id());
                        ch.writeAndFlush(bundle);
                    }
                });
                MatchingHandler.getInstance().submit(wrapper);
            } else {
                ctx.writeAndFlush(ResponseBundle.builder().response(StaticMessage.ERROR_NO_USER_FOUND).responseCode(HttpStatus.FORBIDDEN.value()).build());
                ctx.close();
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
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}

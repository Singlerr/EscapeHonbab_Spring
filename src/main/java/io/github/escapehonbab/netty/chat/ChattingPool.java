package io.github.escapehonbab.netty.chat;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

public class ChattingPool {

    private static ChattingPool instance;
    private final HashMap<String, ChannelHandlerContext> userInConnection;

    private ChattingPool() {
        this.userInConnection = new HashMap<>();
    }

    public static ChattingPool getInstance() {
        if (instance == null)
            return (instance = new ChattingPool());
        return instance;
    }

    public boolean isInConnection(String userId) {
        return userInConnection.containsKey(userId);
    }

    public ChannelHandlerContext getConnection(String userId) {
        return userInConnection.get(userId);
    }

    public void registerConnection(String userId, ChannelHandlerContext ctx) {
        userInConnection.put(userId, ctx);
    }

    public void removeConnection(String userId) {
        if (isInConnection(userId))
            userInConnection.remove(userId);
    }

    public void removeConnection(ChannelHandlerContext ctx) {
        for (String userId : userInConnection.keySet()) {
            if (userInConnection.get(userId).name().equals(ctx.name())) {
                userInConnection.remove(userId);
            }
        }
    }
}

package io.github.escapehonbab.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ChattingPool {

    private static ChattingPool instance;
    private final ChannelGroup userInConnection;

    private final HashMap<String, ChannelId> connected;

    private ChattingPool() {
        this.userInConnection = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.connected = new HashMap<>();
    }

    public boolean isInConnection(ChannelId id) {
        return userInConnection.contains(id);
    }

    public boolean isInConnection(String userId) {
        return connected.containsKey(userId) && userInConnection.contains(connected.get(userId));
    }

    public Channel getConnection(String userId) {
        assert connected.containsKey(userId);
        return userInConnection.find(connected.get(userId));
    }


    public Channel getConnection(ChannelId id) {
        return userInConnection.find(id);
    }

    public void closeConnection(ChannelId id) {
        userInConnection.find(id).closeFuture();
        connected.forEach((k, v) -> {
            if (v.equals(id)) connected.remove(k);
        });
    }
}

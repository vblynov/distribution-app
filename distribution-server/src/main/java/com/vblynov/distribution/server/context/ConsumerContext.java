package com.vblynov.distribution.server.context;

import com.vblynov.distirbution.model.DistributionProtocol;
import io.netty.channel.Channel;
import org.springframework.util.StringUtils;

public class ConsumerContext {

    private final Channel channel;
    private final String token;
    private String consumerName;

    public ConsumerContext(Channel channel, String token) {
        this.channel = channel;
        this.token = token;
    }

    public void send(DistributionProtocol message) {
        if (StringUtils.isEmpty(message.getToken()) && StringUtils.hasLength(token)) {
            channel.writeAndFlush(DistributionProtocol.newBuilder(message).setToken(token));
        } else {
            channel.writeAndFlush(message);
        }
    }

    public String getToken() {
        return token;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }
}

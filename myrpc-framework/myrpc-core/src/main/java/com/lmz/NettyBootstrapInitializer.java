package com.lmz;

import com.lmz.channelHandler.consumerChannelInitializer;
import com.lmz.channelHandler.handler.MySimpleChannelInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 老疯狗
 * @Title NettyBootstrapInitializer
 * @date 2024/8/19
 */
@Slf4j
public class NettyBootstrapInitializer {
    private static Bootstrap bootstrap = new Bootstrap();
    private static NioEventLoopGroup group = new NioEventLoopGroup();

    static {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new consumerChannelInitializer());
    }

    private NettyBootstrapInitializer() {

    }

    public static Bootstrap getBootstrap() {
        return bootstrap;
    }
}

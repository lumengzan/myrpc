package com.lmz.channelHandler.handler;

import com.lmz.MyRPCBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

/**
 * @author 老疯狗
 * @Title MySimpleChannelInboundHandler
 * @date 2024/8/21
 */
public class MySimpleChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        String result=msg.toString(Charset.defaultCharset());
        CompletableFuture<Object> completableFuture = MyRPCBootstrap.PENDING_REQUEST.get(1L);
        completableFuture.complete(result);
    }
}

package com.lmz.proxy.handler;

import com.lmz.MyRPCBootstrap;
import com.lmz.NettyBootstrapInitializer;
import com.lmz.discovery.Registry;
import com.lmz.exception.NetworkException;
import com.lmz.exception.RegistryCenterException;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author 老疯狗
 * @Title RpcConsumerInvocationHandler
 * @date 2024/8/21
 */
@Slf4j
public class RpcConsumerInvocationHandler implements InvocationHandler {

    private Registry registry;
    private Class<?> interfaceRef;

    public RpcConsumerInvocationHandler(Registry registry, Class<?> interfaceRef) {
        this.registry = registry;
        this.interfaceRef = interfaceRef;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable  {


        InetSocketAddress address = registry.lookup(interfaceRef.getName());

        if (log.isDebugEnabled()) {
            log.debug("服务调用方发现了服务【{}】的可用主机的【{}】", interfaceRef.getName(), address.getAddress() + ":" + address.getPort());
        }


        Channel channel=getAvailableChannel(address);
        if (log.isDebugEnabled()) {
            log.debug("获取了和【{}】建立的通道，准备发送数据",address);
        }

        // 异步策略
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        MyRPCBootstrap.PENDING_REQUEST.put(1L, completableFuture);
        channel.writeAndFlush(Unpooled.copiedBuffer("hello".getBytes())).addListener((ChannelFutureListener) promise -> {
            if (!promise.isSuccess()) {
                completableFuture.completeExceptionally(promise.cause());
            }
        });
        return completableFuture.get(10, TimeUnit.SECONDS);
    }

    private Channel getAvailableChannel(InetSocketAddress address) {

        Channel channel = MyRPCBootstrap.CHANNEL_CACHE.get(address);

        if (channel == null) {
            CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
            NettyBootstrapInitializer
                    .getBootstrap()
                    .connect(address)
                    .addListener((ChannelFutureListener) promise -> {
                        if (promise.isDone()) {
                            if (log.isDebugEnabled()) {
                                log.debug("已经和【{}】成功建立了链接", address);
                            }
                            channelFuture.complete(promise.channel());
                        } else if (!promise.isSuccess()) {
                            channelFuture.completeExceptionally(promise.cause());
                        }
                    });
            try {
                channel = channelFuture.get(3, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.error("获取通道时发生异常",e);
                throw new RegistryCenterException(e);
            }


            MyRPCBootstrap.CHANNEL_CACHE.put(address, channel);
        }

        if (channel == null) {
            if (log.isErrorEnabled()) {
                log.error("获取或建立与【{}】的通道时发生异常",address);
            }
            throw new NetworkException("获取通道时发生异常");
        }

        return channel;
    }
}

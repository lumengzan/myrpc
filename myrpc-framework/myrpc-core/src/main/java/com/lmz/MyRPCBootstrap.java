package com.lmz;

import com.lmz.discovery.Registry;
import com.lmz.discovery.RegistryConfig;
import com.lmz.exception.ZookeeperException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 老疯狗
 * @Title MyRPCBootstrap
 * @date 2024/7/14
 */
@Slf4j
public class MyRPCBootstrap {
    public static MyRPCBootstrap myRPCBootstrap = new MyRPCBootstrap();


    private String applicationName = "default";


    private ProtocolConfig protocolConfig;

    private int port = 8088;

    private Registry registry;

    public final static Map<InetSocketAddress, Channel> CHANNEL_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, ServiceConfig<?>> SERVICE_LIST = new ConcurrentHashMap<>(16);

    public static final Map<Long, CompletableFuture<Object>> PENDING_REQUEST = new ConcurrentHashMap<>();

    private MyRPCBootstrap() {
    }

    public static MyRPCBootstrap getInstance() {
        return myRPCBootstrap;
    }

    /*
     * 用来定义当前应用的名字
     * @param appName 应用名称
     * @return this 当前实例
     */
    public MyRPCBootstrap application(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    /*
     * 用来配置一个注册中心
     * @Param registryConfig 注册中心
     * @return this 当前实例
     */
    public MyRPCBootstrap registry(RegistryConfig registryConfig) throws ZookeeperException {

        this.registry = registryConfig.getRegistry();
        return this;
    }

    /*
     * 配置当前服务暴露的协议
     * @Param  暴露的协议
     * @return this
     */
    public MyRPCBootstrap protocol(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
        if (log.isDebugEnabled()) {
            log.debug("当前工程使用了{}协议进行序列化", protocolConfig.toString());
        }
        return this;
    }

    /*
     * 发布服务
     * @param service 服务配置
     * @return this
     */
    public MyRPCBootstrap publish(ServiceConfig<?> service) {

        registry.register(service);
        SERVICE_LIST.put(service.getInterfaceProvider().getName(), service);
        return this;
    }

    /*
     * 批量发布
     * @param serviceList 服务配置列表
     * @return this
     */
    public MyRPCBootstrap publish(List<?> serviceList) {
        return this;
    }

    /*
     * 启动服务
     */
    public void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup(2);
        NioEventLoopGroup worker = new NioEventLoopGroup(10);
        try {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap = serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.info("byteBuf->{}", byteBuf.toString(Charset.defaultCharset()));
                                channelHandlerContext.channel().writeAndFlush(Unpooled.copiedBuffer("myrpc--hello".getBytes()));
                            }
                        });
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

        // channelFuture.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        // finally {
        //     try {
        //         boss.shutdownGracefully().sync();
        //         worker.shutdownGracefully().sync();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    public MyRPCBootstrap reference(ReferenceConfig<?> reference) {

        reference.setRegistry(registry);
        return this;
    }

}

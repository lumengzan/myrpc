package com.lmz;

import com.lmz.discovery.RegistryConfig;
import com.lmz.exception.ZookeeperException;
import com.lmz.impl.HelloMyRPCImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 老疯狗
 * @Title com.lmz.Application
 * @date 2024/7/14
 */
@Slf4j
public class ProviderApplication {

    public static void main(String[] args) throws ZookeeperException {
        ServiceConfig<HelloMyRPC> service = new ServiceConfig<>();
        service.setInterfaceProvider(HelloMyRPC.class);
        service.setRef(new HelloMyRPCImpl());
        log.info("123");
        // 服务提供，方需要注册服务，启动服务
        // 1.封装要发布的服务
        // 2.定义配置中心
        // 3.通过启动引导程序，启动服务提供方
        //     （1）配置 -- 应用的名称 -- 注册中心
        //     （2）发布服务

        MyRPCBootstrap.getInstance()
                .application("first-myrpc-provider")
                // 配置中心
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .protocol(new ProtocolConfig("jdk"))
                // 发布服务
                .publish(service)
                // 启动服务
                .start();
    }
}

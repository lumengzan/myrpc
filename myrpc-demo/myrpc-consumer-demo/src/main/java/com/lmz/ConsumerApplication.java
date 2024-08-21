package com.lmz;

import com.lmz.discovery.RegistryConfig;
import com.lmz.exception.ZookeeperException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 老疯狗
 * @Title Application
 * @date 2024/7/14
 */
// 服务调用方的要求是想尽一切办法获取代理对象
@Slf4j
public class ConsumerApplication {
    public static void main(String[] args) throws ZookeeperException {

        ReferenceConfig<HelloMyRPC> reference = new ReferenceConfig<>();
        reference.setInterface(HelloMyRPC.class);

        MyRPCBootstrap.getInstance()
                .application("first-yrpc-consumer")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(reference);

        HelloMyRPC helloMyRPC = reference.get();
        String sayHi = helloMyRPC.sayHi("你好");
        log.info("sayHi->{}",sayHi);
    }
}

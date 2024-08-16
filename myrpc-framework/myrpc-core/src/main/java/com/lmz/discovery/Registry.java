package com.lmz.discovery;

import com.lmz.ServiceConfig;

import java.net.InetSocketAddress;

/**
 * @author 老疯狗
 * @Title Registery
 * @date 2024/8/13
 */
public interface Registry {


    void register(ServiceConfig<?> serviceConfig);

    InetSocketAddress lookup(String serviceName);
}

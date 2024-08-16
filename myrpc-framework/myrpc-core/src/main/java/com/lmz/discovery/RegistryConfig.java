package com.lmz.discovery;

import com.lmz.Constant;
import com.lmz.discovery.impl.ZookeeperRegistry;
import com.lmz.exception.RegistryCenterException;

/**
 * @author 老疯狗
 * @Title RegistryConfig
 * @date 2024/7/14
 */
public class RegistryConfig {

    //dingyi
    private final String connectString;

    public RegistryConfig(String connectString) {
        this.connectString = connectString;
    }

    public Registry getRegistry() {
        String registryType = getRegistryType(true).toLowerCase().trim();
        if (registryType.equals("zookeeper")) {
            String host = getRegistryType(false);
            return new ZookeeperRegistry(host, Constant.TIME_OUT);
        } else if (registryType.equals("nacos")) {
            return null;
        }
        throw new RegistryCenterException("未发现符合要求的注册中心");
    }

    private String getRegistryType(boolean ifType) {
        String[] typeAndHost = connectString.split("://");
        if (typeAndHost.length != 2) {
            throw new RuntimeException("给定的注册中心连接url不合法");
        }
        return ifType?typeAndHost[0]:typeAndHost[1];
    }

}

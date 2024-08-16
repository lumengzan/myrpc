package com.lmz;

/**
 * @author 老疯狗
 * @Title Contant
 * @date 2024/7/15
 */
public class Constant {
    // zookeeper默认连接地址
    public static final String DEFAULT_ZK_CONNECT="127.0.0.1";

    // 超时时间
    public static final int TIME_OUT=10000;

    // 服务方与调用方在注册中心的基础路径
    public static final String BASE_PROVIDERS_PATH="/myrpc-metadata/provider";
    public static final String BASE_CONSUMERS_PATH="/myrpc-metadata/consumers";
}

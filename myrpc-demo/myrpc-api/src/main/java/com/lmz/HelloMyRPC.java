package com.lmz;

/**
 * @author 老疯狗
 * @Title com.lmz.HelloMyRPC
 * @date 2024/7/14
 */
public interface HelloMyRPC {

    /*
    * 通用接口，server与client都需要依赖
    * @Param msg 发送的消息
    * @return 返回的结果
    */
    String sayHi(String msg);

}

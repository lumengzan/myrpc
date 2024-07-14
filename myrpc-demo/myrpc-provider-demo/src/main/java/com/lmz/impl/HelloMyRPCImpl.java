package com.lmz.impl;

import com.lmz.HelloMyRPC;

/**
 * @author 老疯狗
 * @Title HelloMyRPCImpl
 * @date 2024/7/14
 */
public class HelloMyRPCImpl implements HelloMyRPC {
    public String sayHi(String msg) {
        return "hi consumer:"+msg;
    }
}

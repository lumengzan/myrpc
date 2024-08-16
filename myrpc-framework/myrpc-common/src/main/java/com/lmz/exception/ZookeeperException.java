package com.lmz.exception;

/**
 * @author 老疯狗
 * @Title ZookeeperException
 * @date 2024/7/15
 */
public class ZookeeperException extends RuntimeException{
    public ZookeeperException(Exception e) {
        super(e);
    }
}

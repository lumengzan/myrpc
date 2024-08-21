package com.lmz.exception;

/**
 * @author 老疯狗
 * @Title NetworkExpcetion
 * @date 2024/8/12
 */
public class NetworkException extends RuntimeException{
    public NetworkException() {
        super();
    }

    public NetworkException(String message) {
        super(message);
    }
}

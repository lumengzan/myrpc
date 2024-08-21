package com.lmz.exception;

/**
 * @author 老疯狗
 * @Title RegistryException
 * @date 2024/8/13
 */
public class RegistryCenterException extends RuntimeException{
    public RegistryCenterException() {
    }

    public RegistryCenterException(String message) {
        super(message);
    }

    public RegistryCenterException(Exception e) {
        super(e);
    }
}

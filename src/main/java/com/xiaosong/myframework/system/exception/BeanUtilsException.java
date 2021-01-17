package com.xiaosong.myframework.system.exception;

/**
 * BeanUtils exception.
 *
 * @author johnniang
 */
public class BeanUtilsException extends RuntimeException {

    public BeanUtilsException(String message) {
        super(message);
    }

    public BeanUtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}


package com.xiaosong.myframework.business.exception;

import com.xiaosong.myframework.system.exception.BaseException;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/19
 */

public class BusinessException extends BaseException {

    public BusinessException(String code, String msg) {
        super(code, msg);
    }

    public BusinessException(String code, String msg, Throwable throwble) {
        super(code, msg, throwble);
    }

}

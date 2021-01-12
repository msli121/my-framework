package com.xiaosong.myframework.business.entity;

public class ApiResult {
    //响应码
    private int code;

    public ApiResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

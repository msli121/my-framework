package com.xiaosong.myframework.business.dto;

import lombok.Data;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/29
 */
@Data
public class WeChatLoginDtoEntity {
    private String appId;
    private String appSecret;
    private String code;
    private String state;
    private String grantType;
}

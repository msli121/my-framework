package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.dto.WeChatLoginDtoEntity;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/20
 */
public interface LoginService {
    UserDtoEntity loginByWeChat(WeChatLoginDtoEntity loginDtoEntity);
}

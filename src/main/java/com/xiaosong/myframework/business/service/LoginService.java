package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.dto.WeChatLoginDtoEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.response.UserProfileEntity;
import org.apache.catalina.User;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/20
 */
public interface LoginService {

    UserProfileEntity login(UserEntity user, String userType);

    void passwordRegistry(UserEntity user);

    UserEntity getUserInfoByWeChat(WeChatLoginDtoEntity loginDtoEntity);
}

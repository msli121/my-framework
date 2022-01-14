package com.paradigm.ocr.business.service;

import com.paradigm.ocr.business.dto.WeChatLoginDtoEntity;
import com.paradigm.ocr.business.response.UserProfileEntity;
import com.paradigm.ocr.business.entity.UserEntity;

import java.io.UnsupportedEncodingException;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/20
 */
public interface LoginService {

    UserProfileEntity login(UserEntity user, String userType);

    void passwordRegistry(UserEntity user);

    UserEntity getUserInfoByWeChat(WeChatLoginDtoEntity loginDtoEntity) throws UnsupportedEncodingException;
}

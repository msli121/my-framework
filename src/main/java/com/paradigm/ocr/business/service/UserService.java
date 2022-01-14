package com.paradigm.ocr.business.service;

import com.paradigm.ocr.business.dto.UserDtoEntity;
import com.paradigm.ocr.business.response.UserProfileEntity;
import com.paradigm.ocr.business.entity.UserEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {

    String generateUid();

    void simpleCheckUserIsAuth(String uid);

    void initNewUser(String password, UserEntity user);

    void editUser(UserDtoEntity user);

    void save(UserEntity user);

    void updateUserStatus(UserEntity user);

    void updateUserAvatar(UserEntity user);

    void updateUserBaseInfo(UserEntity user);

    boolean checkExistByUsername(String username);

    boolean checkExistByEmail(String email);

    String generateSysHeadIconRandom();

    String getUserAvatar(String uid);

    UserProfileEntity getUserBaseInfo(String uid) throws UnsupportedEncodingException;

    UserEntity findUserByUsername(String username);

    UserEntity findUserByUid(String uid);

    UserEntity findUserByEmail(String email);

    UserEntity resetPassword(UserEntity user);

    UserEntity findEntityByOpenId(String openId);

    UserEntity findEntityByUnionId(String unionId);

    List<UserDtoEntity> listAllUserDto();

}

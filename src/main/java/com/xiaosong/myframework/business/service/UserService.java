package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.entity.UserEntity;

import java.util.List;

public interface UserService {

    String generateUid();

    void initNewUser(String password, UserEntity user);

    void editUser(UserDtoEntity user);

    void save(UserEntity user);

    void updateUserStatus(UserEntity user);

    boolean isExist(String username);

    String generateSysHeadIconRandom();

    UserEntity findEntityByUsername(String username);

    UserEntity resetPassword(UserEntity user);

    UserEntity findEntityByOpenId(String openId);

    UserEntity findEntityByUnionId(String unionId);

    List<UserDtoEntity> listAllUserDto();

}

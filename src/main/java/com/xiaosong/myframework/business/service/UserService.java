package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.entity.UserEntity;

import java.util.List;

public interface UserService {

    void editUser(UserDtoEntity user);

    List<UserDtoEntity> listAllUserDto();

    boolean isExist(String username);

    UserEntity getByUsername(String username);

    void add(UserEntity user);

    String generateHeadIconRandom();

    void updateUserStatus(UserEntity user);

    UserEntity resetPassword(UserEntity user);

}

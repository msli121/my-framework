package com.paradigm.ocr.business.repository;

import com.paradigm.ocr.business.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */
public interface UserDao  extends JpaRepository<UserEntity,Integer> {

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    UserEntity getByUsernameAndPassword(String username,String password);

    UserEntity findByOpenId(String openId);

    UserEntity findByUnionId(String unionId);

    UserEntity findByUid(String uid);
}

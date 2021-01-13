package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */
public interface UserDao  extends JpaRepository<UserEntity,Integer> {
    UserEntity findByUsername(String username);

    UserEntity getByUsernameAndPassword(String username,String password);
}

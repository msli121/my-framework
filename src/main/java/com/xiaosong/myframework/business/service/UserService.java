package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */

@Service("userService")
public class UserService extends BaseService {

    public boolean isExist(String username) {
        UserEntity user = this.getByUsername(username);
        return null!=user;
    }

    public UserEntity getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public UserEntity getByUsernameAndPassword(String username, String password){
        return userDao.getByUsernameAndPassword(username, password);
    }

    public void add(UserEntity user) {
        userDao.save(user);
    }
}
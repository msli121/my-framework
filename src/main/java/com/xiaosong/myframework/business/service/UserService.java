package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */

@Service("userService")
public class UserService extends BaseService {

    public List<UserDtoEntity> listAllUserDto() {
        List<UserEntity> users = userDao.findAll();

        List<UserDtoEntity> userDtoEntityList = users.stream()
                .map(user -> (UserDtoEntity) new UserDtoEntity().convertFrom(user))
                .collect(Collectors.toList());

        userDtoEntityList.forEach(userDtoEntity -> {
            List<String> roleCodeList = userRoleDao.getAllRoleCodeByUserId(userDtoEntity.getId());
            List<RoleEntity> roles = roleDao.findByRoleCodeIn(roleCodeList);
            userDtoEntity.setRoles(roles);
        });

        return userDtoEntityList;
    }

    public void resetPassword(UserEntity user) {

    }

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

    public String generateHeadIconRandom() {
        int min = 1;
        int max = 9;
        Random random = new Random();
        int randomNumber = random.nextInt(max-min) + min;
        return "head-profile-" + randomNumber + ".jpg";
    }
}
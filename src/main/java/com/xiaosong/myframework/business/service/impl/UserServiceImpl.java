package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import com.xiaosong.myframework.business.service.UserService;
import com.xiaosong.myframework.business.service.base.BaseService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */

@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Override
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

    public void updateUserStatus(UserEntity user) {
        UserEntity userInDB = userDao.findByUsername(user.getUsername());
        userInDB.setEnabled(user.getEnabled());
        userDao.save(userInDB);
    }

    public UserEntity resetPassword(UserEntity user) {
        UserEntity userInDB = userDao.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodedPassword = new SimpleHash("md5", "123", salt, times).toString();
        userInDB.setPassword(encodedPassword);
        return userDao.save(userInDB);
    }

    @Override
    @Transactional
    @Modifying
    public void editUser(UserDtoEntity user) {
        UserEntity userInDB = userDao.findByUsername(user.getUsername());
        userInDB.setPhone(user.getPhone());
        userInDB.setEmail(user.getEmail());
        userDao.save(userInDB);
        userRoleDao.deleteAllByUserId(userInDB.getId());
        List<UserRoleEntity> userRoleEntityList = new ArrayList<>();
        user.getRoles().forEach(r -> {
            UserRoleEntity ur = new UserRoleEntity();
            ur.setUserId(userInDB.getId());
            ur.setRoleCode(r.getRoleCode());
            userRoleEntityList.add(ur);
        });
        userRoleDao.saveAll(userRoleEntityList);
    }

//    @Transactional
//    public void saveUserRoleChange(int userId, List<RoleEntity> roles) {
//        userRoleDao.deleteAllByUserId(userId);
//        List<UserRoleEntity> userRoleEntityList = new ArrayList<>();
//        roles.forEach(r -> {
//            UserRoleEntity ur = new UserRoleEntity();
//            ur.setUserId(userId);
//            ur.setRoleCode(r.getRoleCode());
//            userRoleEntityList.add(ur);
//        });
//        userRoleDao.saveAll(userRoleEntityList);
//    }

}
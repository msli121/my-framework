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

    @Override
    public boolean isExist(String username) {
        UserEntity user = this.findEntityByUsername(username);
        return null!=user;
    }

    @Override
    public UserEntity findEntityByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    public void save(UserEntity user) {
        userDao.save(user);
    }

    @Override
    public String generateSysHeadIconRandom() {
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
    public UserEntity findEntityByOpenId(String openId) {
        return userDao.findByOpenId(openId);
    }

    @Override
    public UserEntity findEntityByUnionId(String unionId) {
        return userDao.findByUnionId(unionId);
    }

    @Override
    public void initNewUser(String password, UserEntity user) {
        // 生成盐,默认长度16位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        // 随机生成用户头像
        user.setSysHeadIcon(generateSysHeadIconRandom());
        user.setEnabled("1");
        user.setLocked("0");
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
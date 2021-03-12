package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.constant.BusinessConstant;
import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.response.UserProfileEntity;
import com.xiaosong.myframework.business.service.UserService;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.business.utils.SysRandomUtil;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
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
    public boolean checkExistByUsername(String username) {
        UserEntity user = this.findUserByUsername(username);
        return null != user;
    }

    @Override
    public boolean checkExistByEmail(String email) {
        UserEntity user = this.findUserByEmail(email);
        return null != user;
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public UserEntity findUserByUid(String uid) {
        return userDao.findByUid(uid);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userDao.findByEmail(email);
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

    @Override
    public String getUserAvatar(String uid) {
        UserEntity userInDb;
        if(StringUtils.isEmpty(uid)) {
            String username = SecurityUtils.getSubject().getPrincipal().toString();
            userInDb = userDao.findByUsername(username);
        } else {
            userInDb = userDao.findByUid(uid);
        }
        if(null == userInDb) {
            throw new BusinessException("002", "登陆异常，请重新登录");
        }
        return userInDb.getAvatar();
    }

    @Override
    public UserProfileEntity getUserBaseInfo(String uid) throws UnsupportedEncodingException {
        UserEntity userInDb;
        if(StringUtils.isEmpty(uid)) {
            String username = SecurityUtils.getSubject().getPrincipal().toString();
            userInDb = userDao.findByUsername(username);
        } else {
            userInDb = userDao.findByUid(uid);
        }
        if(null == userInDb) {
            throw new BusinessException("002", "登陆异常，请重新登录");
        }
        return new UserProfileEntity(userInDb);
    }

    @Override
    public void updateUserStatus(UserEntity user) {
        UserEntity userInDB = userDao.findByUsername(user.getUsername());
        userInDB.setEnabled(user.getEnabled());
        userDao.save(userInDB);
    }

    @Override
    public void updateUserAvatar(UserEntity user) {
        if(StringUtils.isEmpty(user.getUid())) {
            throw new BusinessException("004", "参数异常，缺少UID");
        }
        UserEntity userInDB = userDao.findByUid(user.getUid());
        if(null == userInDB) {
            throw new BusinessException("002", "登陆异常，请重新登录");
        }
        userInDB.setAvatar(user.getAvatar());
        userDao.save(userInDB);
    }

    @Override
    public void updateUserBaseInfo(UserEntity user) {
        if(StringUtils.isEmpty(user.getUid())) {
            throw new BusinessException("004", "参数异常，缺少UID");
        }
        UserEntity userInDB = userDao.findByUid(user.getUid());
        if(null == userInDB) {
            throw new BusinessException("002", "登陆异常，请重新登录");
        }
        userInDB.setBirthday(user.getBirthday());
        userInDB.setCountry(user.getCountry());
        userInDB.setCity(user.getCity());
        userInDB.setSex(user.getSex());
        userInDB.setOrganization(user.getOrganization());
        userDao.save(userInDB);
    }

    @Override
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
    public String generateUid() {
        int computeTime = 3;
        while (computeTime > 0) {
            String randomUid = SysRandomUtil.generateWithCase(BusinessConstant.SYSTEM_UID_LENGTH);
            UserEntity userInDb = userDao.findByUid(randomUid);
            if(null == userInDb) {
                return randomUid;
            }
            computeTime --;
        }
        throw new BusinessException("001", "系统异常，生成用户UID失败");
    }

    @Override
    public void simpleCheckUserIsAuth(String uid) {
        String principal = SecurityUtils.getSubject().getPrincipal().toString();
        if(null == principal) {
            throw new BusinessException("001", "获取用户登录凭证失败，请重新登录");
        }
        if(!uid.equals(principal)) {
            throw new BusinessException("001", "登录异常，请重新登录");
        }
        UserEntity userInDb = userDao.findByUid(uid);
        if(null == userInDb) {
            throw new BusinessException("003", "用户不存在，请重新登录");
        }
    }

    @Override
    public void initNewUser(String password, UserEntity user) {
        // 生成新用户 UID
        user.setUid(generateUid());
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
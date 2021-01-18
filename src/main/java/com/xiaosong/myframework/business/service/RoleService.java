package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.service.base.BaseService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roleService")
public class RoleService extends BaseService {

    public List<RoleEntity> getAllRoles() {
        return roleDao.findAll();
    }

    public List<RoleEntity> getRolesFromCurrentUser() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        UserEntity user = userDao.findByUsername(username);
        List<String> roleCodeList = userRoleDao.getAllRoleCodeByUserId(user.getId());
        return roleDao.findByRoleCodeIn(roleCodeList);
    }

    public List<RoleEntity> getRolesByUsername(String username) {
        UserEntity user = userDao.findByUsername(username);
        List<String> roleCodeList = userRoleDao.getAllRoleCodeByUserId(user.getId());
        return roleDao.findByRoleCodeIn(roleCodeList);
    }
}
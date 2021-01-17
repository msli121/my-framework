package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.MenuEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
}
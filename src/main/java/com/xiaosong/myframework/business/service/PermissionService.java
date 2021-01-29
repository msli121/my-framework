package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.PermissionEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.service.base.BaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("permissionService")
public class PermissionService extends BaseService {

    public void save(PermissionEntity p) {
        permissionDao.save(p);
    }

    public void updatePermissionStatus(PermissionEntity permission) {
        PermissionEntity roleInDB = permissionDao.findByPermissionCode(permission.getPermissionCode());
        roleInDB.setEnabled(permission.getEnabled());
        permissionDao.save(roleInDB);
    }

    public List<PermissionEntity> getAllPermission() {
       return permissionDao.findAll();
    }

    public List<PermissionEntity> getAllPermissionWithTree() {
        List<PermissionEntity> permissions = permissionDao.findAll();
        adjustPermissionStruct(permissions);
        return permissions;
    }

    public List<PermissionEntity> getAllPermissionByRoleCode(String roleCode) {
        List<String> permissionCodes = rolePermissionDao.getAllPermissionCodeByRoleCode(roleCode);
        return permissionDao.getAllPermissionByPermissionCodes(permissionCodes);
    }

    public List<String> listPermissionUrlByUserName(String username) {
        List<String> permissions= new ArrayList<>();
        UserEntity user = userDao.findByUsername(username);
        if(user != null) {
            permissions = listPermissionUrlByUserId(user.getId());
        }
        return permissions;
    }

    public List<String> listPermissionUrlByUserId(int userId) {
        List<String> roleCodeList = userRoleDao.getAllRoleCodeByUserId(userId);
        List<String> permissionCodeList = rolePermissionDao.getAllPermissionCodeByRoleCodes(roleCodeList);
        return permissionDao.getAllPermissionUrlByPermissionCode(permissionCodeList);
    }

    /**
     * Determine whether client requires permission when requests
     * a certain API.
     * @param requestAPI API requested by client
     * @return true when requestAPI is found in the DB
     */
    public boolean needFilter(String requestAPI) {
        List<PermissionEntity> permissionEntityList = permissionDao.findAll();
        for (PermissionEntity p: permissionEntityList) {
            // match prefix
            if (requestAPI.startsWith(p.getUrl())) {
                return true;
            }
        }
        return false;
    }

    private void adjustPermissionStruct(List<PermissionEntity> permissions) {
        permissions.forEach(item -> {
            List<PermissionEntity> children = permissionDao.findAllByParentPermissionCode(item.getParentPermissionCode());
            item.setChildren(children);
        });
        permissions.removeIf(item -> StringUtils.isNotEmpty(item.getParentPermissionCode()));
    }
}

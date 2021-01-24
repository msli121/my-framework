package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.dto.RoleDtoEntity;
import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.entity.*;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.service.base.BaseService;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.tags.RoleTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Service("roleService")
@Log4j2
public class RoleService extends BaseService {

    @Autowired
    MenuService menuService;

    @Autowired
    PermissionService permissionService;

    public List<RoleDtoEntity> getAllRoles() {
        List<RoleEntity> roles = roleDao.findAll();

        List<RoleDtoEntity> roleDtoEntityList = roles.stream()
                .map(role -> (RoleDtoEntity) new RoleDtoEntity().convertFrom(role))
                .collect(Collectors.toList());

        roleDtoEntityList.forEach(roleDto -> {
            List<MenuEntity> menus = menuService.getMenuByRoleCode(roleDto.getRoleCode());
            roleDto.setMenus(menus);
            List<PermissionEntity> permissions = permissionService.getAllPermissionByRoleCode(roleDto.getRoleCode());
            roleDto.setPermissions(permissions);
        });
        return roleDtoEntityList;
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

    @Modifying
    @Transactional
    public void updateRoleMenu(String roleCode, List<String> menuCodes) {
        roleMenuDao.deleteAllByRoleCode(roleCode);
        List<RoleMenuEntity> rms = new ArrayList<>();
        for (String menuCode : menuCodes) {
            RoleMenuEntity rm = new RoleMenuEntity();
            rm.setRoleCode(roleCode);
            rm.setMenuCode(menuCode);
            rms.add(rm);
        }
        roleMenuDao.saveAll(rms);
    }

    public RoleEntity updateRoleStatus(RoleEntity role) {
        RoleEntity roleInDB = roleDao.findByRoleCode(role.getRoleCode());
        roleInDB.setEnabled(role.getEnabled());
        return roleDao.save(roleInDB);
    }

    public void addOrUpdate(RoleDtoEntity role) throws InvocationTargetException, IllegalAccessException {
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(roleEntity, role);
        roleDao.save(roleEntity);
    }

    @Transactional
    @Modifying
    public void addOrUpdateRoleMenu(RoleDtoEntity role) {
        // 先删除原有菜单
        roleMenuDao.deleteAllByRoleCode(role.getRoleCode());
        List<RoleMenuEntity> entities = new ArrayList<>();
        role.getMenus().forEach(menu -> {
            RoleMenuEntity item = new RoleMenuEntity();
            item.setRoleCode(role.getRoleCode());
            item.setMenuCode(menu.getMenuCode());
            item.setDescription("角色 [ " + role.getRoleCode() + " ] 拥有菜单 [ " + menu.getMenuCode() + " ]");
            entities.add(item);
        });
        roleMenuDao.saveAll(entities);
//        log.info("保存所有的roleMenuDao");
    }

    @Transactional
    @Modifying
    public void addOrUpdateRolePermission(RoleDtoEntity role) {
        // 先删除原有权限
        rolePermissionDao.deleteAllByRoleCode(role.getRoleCode());
        List<RolePermissionEntity> rolePermissionEntityList = new ArrayList<>();
        role.getPermissions().forEach(p -> {
            RolePermissionEntity entity = new RolePermissionEntity();
            entity.setRoleCode(role.getRoleCode());
            entity.setPermissionCode(p.getPermissionCode());
            entity.setDescription("角色 [ " + role.getRoleCode() + " ] 拥有权限 [ " + p.getPermissionCode() + " ]");
            rolePermissionEntityList.add(entity);

        });
        rolePermissionDao.saveAll(rolePermissionEntityList);
//        log.info("保存所有的rolePermissionEntityList");
    }
}
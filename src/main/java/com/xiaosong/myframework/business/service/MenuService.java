package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.MenuEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.business.service.impl.UserServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.List;

@Service("menuService")
public class MenuService extends BaseService {
    @Autowired
    public UserServiceImpl userService;

    public List<MenuEntity> getAllByParentMenuCode(String parentMenuCode) {
        return menuDao.findAllByParentMenuCode(parentMenuCode);
    }

    public List<MenuEntity> getAllMenus() {
        return menuDao.findAll();
    }

    public List<MenuEntity> getAllMenusWithTree() {
        List<MenuEntity> menus = menuDao.findAll();
        adjustMenuStruct(menus);
        return menus;
    }

    public List<MenuEntity> getMenusFromCurrentUser() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        UserEntity user = userDao.findByUsername(username);
        // find all roleCode from current user
        List<String>  roleCodeList =  userRoleDao.findAllByUserId(user.getId())
                .stream().map(UserRoleEntity::getRoleCode).collect(Collectors.toList());
        // find all menuCode from current user
        List<String> menuCodeList = roleMenuDao.getAllMenuCodeByRoleCodes(roleCodeList);
        List<MenuEntity> menus = menuDao.findByMenuCodeIn(menuCodeList).stream()
                .distinct().collect(Collectors.toList());
        return menus;
    }

    public List<MenuEntity> getMenusFromCurrentUserWithTree() {
        List<MenuEntity> menus = getMenusFromCurrentUser();
        adjustMenuStruct(menus);
        return menus;
    }


    public List<MenuEntity> getMenuByRoleCode(String roleCode) {
        List<String> menuCodeList = roleMenuDao.getAllMenuCodeByRoleCode(roleCode);
        List<MenuEntity> menus = menuDao.findByMenuCodeIn(menuCodeList);

        adjustMenuStruct(menus);
        return menus;
    }

    /**
     * Adjust the Structure of the menu.
     * @param menus
     */
    public void adjustMenuStruct(List<MenuEntity> menus) {
        menus.forEach(item -> {
            List<MenuEntity> children = getAllByParentMenuCode(item.getMenuCode());
            item.setChildren(children);
        });
        menus.removeIf(item -> StringUtils.isNotEmpty(item.getParentMenuCode()));
    }

}

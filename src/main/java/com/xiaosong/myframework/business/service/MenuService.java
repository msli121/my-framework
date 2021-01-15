package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.MenuEntity;
import com.xiaosong.myframework.business.entity.RoleMenuEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import com.xiaosong.myframework.business.repository.MenuDao;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.lang.Integer;
import java.util.List;

@Service("menuService")
public class MenuService extends BaseService{
    @Autowired
    public UserService userService;

    public List<MenuEntity> getAllByParentId(int parentId) {
        return menuDao.findAllByParentId(parentId);
    }

    public List<MenuEntity> getMenusFromCurrentUser() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        UserEntity user = userDao.findByUsername(username);
        // 查询用户所有角色的id
        List<Integer>  roleIds =  userRoleDao.findAllByUserId(user.getId())
                .stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        // 查询角色所具有的菜单id
        List<Integer> menuIds = roleMenuDao.findMenuIdsByRoleIds(roleIds);
        List<MenuEntity> menus = menuDao.findAllById(menuIds).stream().distinct().collect(Collectors.toList());

        adjustMenuStruct(menus);

        return menus;
    }

    public List<MenuEntity> getMenuByRoleId(int roleId) {
        List<Integer> menuIds = roleMenuDao.findAllByRoleId(roleId)
                .stream().map(RoleMenuEntity::getMenuId).collect(Collectors.toList());
        List<MenuEntity> menus = menuDao.findAllById(menuIds);

        adjustMenuStruct(menus);
        return menus;
    }

    /**
     * Adjust the Structure of the menu.
     * @param menus
     */
    public void adjustMenuStruct(List<MenuEntity> menus) {
        menus.forEach(item -> {
            List<MenuEntity> children = getAllByParentId(item.getId());
            item.setChildren(children);
        });
        menus.removeIf(item -> item.getParentId() != 0);
    }

}

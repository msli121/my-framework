package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.service.MenuService;
import com.xiaosong.myframework.business.service.RoleService;
import com.xiaosong.myframework.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;

    /**
     * 获取所有用户
     * @return
     */
    @GetMapping("/users")
    public ApiResult getAllUser() {
        return ApiResult.T(userService.listAllUserDto());
    }

    /**
     * 获取所有所有菜单
     * @return
     */
    @GetMapping("/menus/all")
    public ApiResult getAllMenus() {
        return ApiResult.T(menuService.getAllMenus());
    }

    /**
     * 获取当前登录用户的所有菜单
     * @return
     */
    @GetMapping("/menus")
    public ApiResult getMenusFromCurrentUser() {
        return ApiResult.T(menuService.getMenusFromCurrentUser());
    }

    /**
     * 获取系统所有的角色
     * @return
     */
    @GetMapping("/roles/all")
    public ApiResult getAllRoles() {
        return ApiResult.T(roleService.getAllRoles());
    }

    /**
     * 获取当前用户具有的角色
     * @return
     */
    @GetMapping("/roles")
    public ApiResult getRolesFromCurrentUser() {
        return ApiResult.T(roleService.getRolesFromCurrentUser());
    }
}

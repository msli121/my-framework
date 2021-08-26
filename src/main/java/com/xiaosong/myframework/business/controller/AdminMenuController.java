package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ocr/admin/menu")
public class AdminMenuController {
    @Autowired
    MenuService menuService;

    /**
     * 获取系统所有菜单，不展示菜单之间的层次结构
     * @return
     */
    @GetMapping("/all")
    public ApiResult getAllMenu() {
        return ApiResult.T(menuService.getAllMenus());
    }

    /**
     * 获取系统所有菜单，展示菜单之间的层次结构
     * @return
     */
    @GetMapping("/all/tree")
    public ApiResult getAllMenuWithTree() {
        return ApiResult.T(menuService.getAllMenusWithTree());
    }

    /**
     * 获取当前登录用户所有菜单
     * @return
     */
    @GetMapping("/current")
    public ApiResult getMenusFromCurrentUser() {
        return ApiResult.T(menuService.getMenusFromCurrentUser());
    }

    /**
     * 获取当前登录用户所有菜单
     * @return
     */
    @GetMapping("/current/tree")
    public ApiResult getMenusFromCurrentUserWithTree() {
        return ApiResult.T(menuService.getMenusFromCurrentUserWithTree());
    }

}

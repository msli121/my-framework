package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/menu")
public class AdminMenuController {
    @Autowired
    MenuService menuService;

    /**
     * 获取系统所有菜单
     * @return
     */
    @GetMapping("/all")
    public ApiResult getAllMenus() {
        return ApiResult.T(menuService.getAllMenus());
    }

    /**
     * 获取当前登录用户所有菜单
     * @return
     */
    @GetMapping("/current")
    public ApiResult getMenusFromCurrentUser() {
        return ApiResult.T(menuService.getMenusFromCurrentUser());
    }


}

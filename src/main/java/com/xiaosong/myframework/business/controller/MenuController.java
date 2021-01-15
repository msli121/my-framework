package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.entity.ApiResult;
import com.xiaosong.myframework.business.service.MenuService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/15
 */

public class MenuController {
    @Autowired
    public MenuService menuService;

    @GetMapping("/api/menu")
    public ApiResult menu() {
        return ApiResult.T(menuService.getMenusFromCurrentUser());
    }

    @GetMapping("/api/admin/role/menu")
    public ApiResult listAllMenus() {
        return ApiResult.T(menuService.getMenuByRoleId(1));
    }
}

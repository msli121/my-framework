package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.entity.ApiResult;
import com.xiaosong.myframework.business.service.MenuService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/15
 */
@RestController
@RequestMapping("/api")
public class MenuController {
    @Autowired
    public MenuService menuService;

    @GetMapping("/menu")
    public ApiResult menu() {
        return ApiResult.T(menuService.getMenusFromCurrentUser());
    }

    @GetMapping("/admin/role/menu")
    public ApiResult listAllMenus() {
        return ApiResult.T(menuService.getMenuByRoleCode("ROLE_ADMIN"));
    }
}

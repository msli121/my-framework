package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/role")
public class AdminRoleController {
    @Autowired
    RoleService roleService;

    /**
     * 获取当前登录用户所有角色
     * @return
     */
    @GetMapping("/all")
    public ApiResult getRolesFromCurrentUser() {
        return ApiResult.T(roleService.getRolesFromCurrentUser());
    }


    /**
     * 获取当前登录用户所有角色
     * @return
     */
    @GetMapping("/current")
    public ApiResult getMenusFromCurrentUser() {
        return ApiResult.T(roleService.getRolesFromCurrentUser());
    }


}

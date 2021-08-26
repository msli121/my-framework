package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.dto.RoleDtoEntity;
import com.xiaosong.myframework.business.entity.PermissionEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/api/ocr/admin/permission")
public class AdminPermissionController {
    @Autowired
    PermissionService permissionService;

    /**
     * 获取系统所有权限
     * @return
     */
    @GetMapping("/all")
    public ApiResult getAllPermission() {
        return ApiResult.T(permissionService.getAllPermission());
    }

    /**
     * 获取当前登录用户所有菜单
     * @return
     */
    @GetMapping("/all/tree")
    public ApiResult getAllPermissionWithTree() {
        return ApiResult.T(permissionService.getAllPermissionWithTree());
    }

    /**
     * 修改或新增权限
     * @param permission
     * @return null
     */
    @PostMapping("/edit-or-add")
    public ApiResult editOrAddRole(@RequestBody PermissionEntity permission) {
        permissionService.save(permission);
        return ApiResult.T("操作成功");
    }

    /**
     * 更新权限状态
     * @param permission
     * @return
     */
    @PostMapping("/status")
    public ApiResult updateRoleStatus(@RequestBody PermissionEntity permission) {
        permissionService.updatePermissionStatus(permission);
        return ApiResult.T("权限状态更新成功");
    }
}

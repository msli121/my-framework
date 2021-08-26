package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.dto.RoleDtoEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ocr/admin/role")
public class AdminRoleController {
    @Autowired
    RoleService roleService;

    /**
     * 获取系统用户所有角色
     * @return
     */
    @GetMapping("/all")
    public ApiResult getRolesFromCurrentUser() {
        return ApiResult.T(roleService.getAllRoles());
    }

    /**
     * 获取当前登录用户所有角色
     * @return
     */
    @GetMapping("/current")
    public ApiResult getMenusFromCurrentUser() {
        return ApiResult.T(roleService.getRolesFromCurrentUser());
    }

    /**
     * 更新角色菜单权限
     * @param roleMenuMap
     * @return
     */
    @PostMapping("/update-menu/{roleCode}")
    public ApiResult updateRoleMenu(@PathVariable String roleCode, @RequestBody Map<String, List<String>> roleMenuMap) {
        List<String> menuCodes = roleMenuMap.get("menuCodes");
        roleService.updateRoleMenu(roleCode, menuCodes);
        return ApiResult.T("角色菜单权限更新成功");
    }

    /**
     * 更新角色状态
     * @param role
     * @return
     */
    @PostMapping("/status")
    public ApiResult updateRoleStatus(@RequestBody RoleEntity role) {
        roleService.updateRoleStatus(role);
        return ApiResult.T("角色状态更新成功");
    }

    /**
     * 修改或新增角色
     * @param role
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping("/edit-or-add")
    public ApiResult editOrAddRole(@RequestBody RoleDtoEntity role) throws InvocationTargetException, IllegalAccessException {
        roleService.addOrUpdate(role);
        roleService.addOrUpdateRoleMenu(role);
        roleService.addOrUpdateRolePermission(role);
        return ApiResult.T("操作成功");
    }

}

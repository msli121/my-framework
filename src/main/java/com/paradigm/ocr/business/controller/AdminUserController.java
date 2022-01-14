package com.paradigm.ocr.business.controller;

import com.paradigm.ocr.business.dto.ApiResult;
import com.paradigm.ocr.business.dto.UserDtoEntity;
import com.paradigm.ocr.business.entity.UserEntity;
import com.paradigm.ocr.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    UserService userService;

    /**
     * 获取系统所有用户
     * @return
     */
    @GetMapping("/all")
    public ApiResult getAllUser() {
        return ApiResult.T(userService.listAllUserDto());
    }

    /**
     * 更新用户状态
     * @param requestUser
     * @return
     */
    @PostMapping("/status")
    public ApiResult updateUserStatus(@RequestBody UserEntity requestUser) {
        userService.updateUserStatus(requestUser);
        return ApiResult.T("用户状态更新成功");
    }

    /**
     * 重置密码
     * @param requestUser
     * @return
     */
    @PostMapping("/reset")
    public ApiResult resetPassword(@RequestBody UserEntity requestUser) {
        userService.resetPassword(requestUser);
        return ApiResult.T("重置密码成功");
    }

    /**
     * 修改用户信息
     * @param requestUser
     * @return
     */
    @PostMapping("/edit")
    public ApiResult editUser(@RequestBody UserDtoEntity requestUser) {
        userService.editUser(requestUser);
        return ApiResult.T("修改用户信息成功");
    }
}

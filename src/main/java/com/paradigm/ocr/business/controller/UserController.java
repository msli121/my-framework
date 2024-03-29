package com.paradigm.ocr.business.controller;

import com.paradigm.ocr.business.dto.ApiResult;
import com.paradigm.ocr.business.entity.UserEntity;
import com.paradigm.ocr.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 获取用户基本信息
     * @param uid
     * @return
     */
    @GetMapping("/base-info/{uid}")
    public ApiResult getUserBaseInfo(@PathVariable String uid) throws UnsupportedEncodingException {
        return ApiResult.T(userService.getUserBaseInfo(uid));
    }

    /**
     * 重置密码
     * @param user
     * @return
     */
    @GetMapping("/password")
    public ApiResult resetPassword(@RequestBody UserEntity user) {
        userService.resetPassword(user);
        return ApiResult.T();
    }

    /**
     * 更新头像
     * @param user
     * @return
     */
    @PostMapping("/avatar/update")
    public ApiResult updateAvatar(@RequestBody UserEntity user) {
        userService.updateUserAvatar(user);
        return ApiResult.T();
    }

    @GetMapping("/avatar/get/{uid}")
    public ApiResult getUserAvatar(@PathVariable String uid) {
        return ApiResult.T(userService.getUserAvatar(uid));
    }

    /**
     * 更新用户基本信息
     * @param user
     * @return
     */
    @PostMapping("/update-info")
    public ApiResult updateUserBaseInfo(@RequestBody UserEntity user) {
        userService.updateUserBaseInfo(user);
        return ApiResult.T();
    }
}

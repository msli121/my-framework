package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping(value = "/hello")
    ApiResult helloWorld() {
        return ApiResult.T("hello world");
    }
}

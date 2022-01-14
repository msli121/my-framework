package com.paradigm.ocr.business.controller;

import com.paradigm.ocr.business.dto.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping(value = "/test")
    ApiResult helloWorld() {
        return ApiResult.T("hello world");
    }
}

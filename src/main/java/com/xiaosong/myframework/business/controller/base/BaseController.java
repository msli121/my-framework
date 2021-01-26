package com.xiaosong.myframework.business.controller.base;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
public class BaseController {
    @Value("${ocr.url}")
    protected String ocrHostUrl;
}

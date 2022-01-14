package com.paradigm.ocr.business.controller.base;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
public class BaseController {
    @Value("${ocr.url}")
    protected String ocrApiUrl;

    @Value("${pdf.url}")
    protected String pdfApiUrl;

    @Value("${dns.url}")
    protected String dnsUrl;
}

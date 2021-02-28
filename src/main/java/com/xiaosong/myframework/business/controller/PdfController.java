package com.xiaosong.myframework.business.controller;

import com.alibaba.fastjson.JSON;
import com.xiaosong.myframework.business.controller.base.BaseController;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.service.OcrService;
import com.xiaosong.myframework.business.service.PdfService;
import com.xiaosong.myframework.business.service.SysFileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@RestController
@RequestMapping("/api/pdf")
@Log4j2
public class PdfController extends BaseController {

    @Autowired
    SysFileService sysFileService;

    @Autowired
    PdfService pdfService;

    @PostMapping("/pdf-upload")
    public ApiResult uploadPdfFile(@RequestParam(value="uid") String uid, @RequestParam("file") MultipartFile file) throws IOException {
        return ApiResult.T(pdfService.getOcrRecognitionResult(file, uid, pdfApiUrl));
    }

    @PostMapping("/edit-save")
    public ApiResult editAndSaveRecognitionResult(@RequestBody SysFileEntity file) {
        sysFileService.editAndSave(file);
        return ApiResult.T("保存成功");
    }

}

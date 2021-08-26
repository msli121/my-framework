package com.xiaosong.myframework.business.controller;

import com.alibaba.fastjson.JSON;
import com.xiaosong.myframework.business.controller.base.BaseController;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.service.OcrService;
import com.xiaosong.myframework.business.service.SysFileService;
import com.xiaosong.myframework.business.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@RestController
@RequestMapping("/api/ocr")
@Log4j2
public class OcrController  extends BaseController {

    @Autowired
    SysFileService sysFileService;

    @Autowired
    UserService userService;

    @Autowired
    OcrService ocrService;

    @PostMapping("/upload-single/file")
    public ApiResult uploadSingleMultipartFileToRecognize(@RequestParam(value="uid") String uid,@RequestParam("file") MultipartFile file) throws IOException {
        userService.simpleCheckUserIsAuth(uid);
        SysFileEntity sysFileEntity = ocrService.RecognizeSingleImageAndSave(ocrApiUrl, uid, file);
        return ApiResult.T(sysFileEntity);
    }

    @PostMapping("/upload-single/base64")
    public ApiResult uploadSinglePicture(@RequestBody SysFileEntity file) {
        userService.simpleCheckUserIsAuth(file.getUid());
        SysFileEntity recognitionResult = ocrService.getOcrRecognitionResult(ocrApiUrl, file);
        return ApiResult.T(recognitionResult);
    }

    @PostMapping("/upload-single/base64-not-save")
    public ApiResult uploadSinglePictureWithSave(@RequestBody SysFileEntity file) {
        SysFileEntity recognitionResult = ocrService.getOcrRecognitionResultWithout(ocrApiUrl, file);
        return ApiResult.T(recognitionResult);
    }

    @PostMapping("/edit-save")
    public ApiResult editAndSaveRecognitionResult(@RequestBody SysFileEntity file) {
        sysFileService.editAndSave(file);
        return ApiResult.T("保存成功");
    }

}

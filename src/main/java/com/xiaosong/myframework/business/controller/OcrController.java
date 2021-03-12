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

    @PostMapping("/upload-single")
    public ApiResult uploadSingleImageToRecognize(@RequestParam(value="uid") String uid,@RequestParam("file") MultipartFile file) {
        userService.simpleCheckUserIsAuth(uid);
        return ApiResult.T();
    }

    @PostMapping("/single-upload")
    public ApiResult uploadSinglePicture(@RequestBody SysFileEntity file) {
        file.setUploadTime(new Timestamp(System.currentTimeMillis()));
        Object recognitionResult = ocrService.getOcrRecognitionResult(ocrApiUrl, file);
        file.setRecognitionContent(JSON.toJSONString(recognitionResult));
        sysFileService.save(file);
        return ApiResult.T(file);
    }

    @PostMapping("/edit-save")
    public ApiResult editAndSaveRecognitionResult(@RequestBody SysFileEntity file) {
        sysFileService.editAndSave(file);
        return ApiResult.T("保存成功");
    }

}

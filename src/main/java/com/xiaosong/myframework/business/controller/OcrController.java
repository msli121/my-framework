package com.xiaosong.myframework.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaosong.myframework.business.controller.base.BaseController;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.service.OcrService;
import com.xiaosong.myframework.business.service.SysFileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

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
    OcrService ocrService;

    @PostMapping("/single-upload")
    public ApiResult uploadSinglePicture(@RequestBody SysFileEntity file) {
        file.setUploadTime(new Timestamp(System.currentTimeMillis()));
        sysFileService.save(file);
        Object recognitionResult = ocrService.getOcrRecognitionResult(ocrHostUrl, file);
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

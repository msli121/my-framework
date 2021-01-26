package com.xiaosong.myframework.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaosong.myframework.business.controller.base.BaseController;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.service.OcrService;
import com.xiaosong.myframework.business.service.UploadFileService;
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
    UploadFileService uploadFileService;

    @Autowired
    OcrService ocrService;

    @PostMapping("/single")
    public ApiResult uploadSinglePicture(@RequestBody SysFileEntity file) {
        file.setCreateTime(new Timestamp(System.currentTimeMillis()));
        uploadFileService.save(file);
        HashMap<String, Object> requestBody = new HashMap<>();
        ArrayList<String> images = new ArrayList<>();
        images.add(file.getFileData());
        requestBody.put("images", images);
        String ocrResult = ocrService.getOcrRecognitionResult(ocrHostUrl, JSON.toJSONString(requestBody));
        JSONObject result = JSON.parseObject(ocrResult);
        JSONArray data = result.getJSONArray("results");
        return ApiResult.T(data.get(0));
    }
}

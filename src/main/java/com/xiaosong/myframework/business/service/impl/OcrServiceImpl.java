package com.xiaosong.myframework.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.service.OcrService;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.system.utils.SysHttpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@Service("ocrService")
@Transactional(rollbackFor = Exception.class)
public class OcrServiceImpl extends BaseService implements OcrService {

    @Override
    public String getOcrRecognitionResult(String url, String jsonData) {
        return SysHttpUtils.getInstance().sendJsonPost(url, jsonData);
    }

    @Override
    public Object getOcrRecognitionResult(String ocrApiUrl, SysFileEntity fileEntity) {
        HashMap<String, Object> requestBody = new HashMap<>();
        ArrayList<String> images = new ArrayList<>();
        if(StringUtils.isEmpty(fileEntity.getFileContent())) {
            throw new BusinessException("004", "文件内容不能为空");
        }
        if(fileEntity.getFileContent().split(",").length <= 1) {
            throw new BusinessException("004", "文件内容需要base64编码");
        }
        String base64 = fileEntity.getFileContent().split(",")[1];
        images.add(base64);
        requestBody.put("images", images);
        String ocrResult = SysHttpUtils.getInstance().sendJsonPost(ocrApiUrl, JSON.toJSONString(requestBody));
        JSONObject result = JSON.parseObject(ocrResult);
        return result.getJSONArray("results").get(0);
    }
}

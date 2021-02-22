package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.SysFileEntity;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
public interface OcrService {
    String getOcrRecognitionResult(String url, String jsonData);

    Object getOcrRecognitionResult(String ocrApiUrl, SysFileEntity fileEntity);
}

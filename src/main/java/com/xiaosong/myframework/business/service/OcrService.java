package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
public interface OcrService {
    SysFileEntity uploadSingleImageToRecognize(String uid, MultipartFile file);

    Object getOcrRecognitionResult(String ocrApiUrl, SysFileEntity fileEntity);

}

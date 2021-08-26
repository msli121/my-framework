package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
public interface OcrService {

    SysFileEntity RecognizeSingleImageAndSave(String ocrApiUrl, String uid, MultipartFile file) throws IOException;

    SysFileEntity getOcrRecognitionResult(String ocrApiUrl, SysFileEntity fileEntity);

    SysFileEntity getOcrRecognitionResultWithout(String ocrApiUrl, SysFileEntity fileEntity);

}

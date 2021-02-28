package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/02/28
 */
public interface PdfService {

    List<HashMap<String, Object>> getOcrRecognitionResult(MultipartFile file, String uid, String pdfApiUrl) throws IOException;

}

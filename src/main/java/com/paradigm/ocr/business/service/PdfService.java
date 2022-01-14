package com.paradigm.ocr.business.service;

import com.paradigm.ocr.business.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author msli
 * @Date 2021/02/28
 */
public interface PdfService {

    List<HashMap<String, Object>> recognisePdfFivePage(MultipartFile file, String uid, String pdfApiUrl) throws IOException;

    HashMap<String, Object>  recogniseBase64Pdf(SysFileEntity file, String pdfApiUrl);

    List<HashMap<String, Object>> recogniseUrlPdfFivePage(Map map, String pdfApiUrl) throws IOException;

    List<HashMap<String, Object>> recogniseUrlPdfFivePageWithoutLogin(String pdfUrl, String pdfApiUrl) throws IOException;
}

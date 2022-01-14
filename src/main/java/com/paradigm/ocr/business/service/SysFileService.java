package com.paradigm.ocr.business.service;

import com.paradigm.ocr.business.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */

public interface SysFileService {


    List<String> uploadFileToLocalServer(String uid, MultipartFile[] multipartFiles, String dnsUrl);

    void classifyPdfDocument(MultipartFile file, HttpServletResponse response, String ocrApiUrl) throws IOException;

    void downloadLocalServerFile(String uid, String fileName, HttpServletResponse response);

    void editAndSave(SysFileEntity file);

    void save(SysFileEntity file);

    void setFileStar(Integer fileId);

    void cancelFileStar(Integer fileId);

    void deleteFile(Integer fileId);

    List<SysFileEntity> getAllUploadFile(String uid);
}

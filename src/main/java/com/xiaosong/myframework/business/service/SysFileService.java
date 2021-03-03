package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */

public interface SysFileService {

    List<String> uploadFileToLocalServer(String uid, MultipartFile[] multipartFiles, String dnsUrl);

    void downloadLocalServerFile(String uid, String fileName, HttpServletResponse response);

    void editAndSave(SysFileEntity file);

    void save(SysFileEntity file);

    void setFileStar(Integer fileId);

    void cancelFileStar(Integer fileId);

    void deleteFile(Integer fileId);

    List<SysFileEntity> getAllUploadFile(String uid);
}

package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.repository.SysFileDao;
import com.xiaosong.myframework.business.service.SysFileService;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.business.utils.FileUtil;
import com.xiaosong.myframework.business.utils.SysRandomUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@Service("sysFileService")
@Transactional(rollbackFor = Exception.class)
public class SysFileServiceImpl extends BaseService implements SysFileService {

    @Autowired
    public SysFileDao sysFileDao;

    @Override
    public List<String> uploadFileToLocalServer(String uid, MultipartFile[] multipartFile, String dnsUrl) {
        UserEntity userInDb = userDao.findByUid(uid);
        if(null == userInDb) {
            throw new BusinessException("003", "用户不存在，请重新登录");
        }
        String os = System.getProperty("os.name");
        String fileRootDir = "";
        if(os.toLowerCase().startsWith("win")) {
            fileRootDir = "D:"+ File.separator+"ocr-file"+File.separator+"public"+File.separator + uid + File.separator;
        } else {
            fileRootDir = "/home/msli/wwwapps/ocr-file/public/" + uid + "/";
        }
        // 校验文件夹是否存在
        File folder = new File(fileRootDir);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        List<String> fileUrlList = new ArrayList<>();
        if(multipartFile != null && multipartFile.length > 0) {
            for (int i = 0; i < multipartFile.length; i++) {
                try {
                    String fileOldName = multipartFile[i].getOriginalFilename();
                    String fileNewName;
                    if(StringUtils.isEmpty(fileOldName) || fileOldName.lastIndexOf(".") == 0) {
                        fileNewName = SysRandomUtil.getRandomString(6);
                    } else {
                        fileNewName = fileOldName.substring(0, fileOldName.lastIndexOf("."))  + "-" + SysRandomUtil.getRandomString(6) + fileOldName.substring(fileOldName.lastIndexOf("."));
                    }
                    File newFile = new File(fileRootDir, fileNewName);
                    multipartFile[i].transferTo(newFile);
                    String fileUrl = dnsUrl + "/api/file/" + uid + "/" + fileNewName;
                    fileUrlList.add(fileUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessException("001", "上传失败，服务器开小差了");
                }
            }
        }
        return fileUrlList;
    }

    @Override
    public void downloadLocalServerFile(String uid, String fileName, HttpServletResponse response) {
        UserEntity userInDb = userDao.findByUid(uid);
        if(null == userInDb) {
            throw new BusinessException("003", "用户不存在，请重新登录");
        }
        String os = System.getProperty("os.name");
        String fileRootDir = "";
        if(os.toLowerCase().startsWith("win")) {
            fileRootDir = "D:/ocr/user-file/" + uid + "/" + fileName;
        } else {
            fileRootDir = "/home/msli/wwwapps/ocr/user-file/" + uid + "/" + fileName;
        }
        // 校验文件夹是否存在
        File downloadFile = new File(fileRootDir);
        if (!downloadFile.exists()) {
            throw new BusinessException("001", "出错啦，文件不存在");
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
        response.setContentType("application/octet-stream;charset=UTF-8");

        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName.trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new BusinessException("001", "下载失败，服务开小差了");
        }

        OutputStream outputStream = null;
        BufferedInputStream bis = null;
        try {
            byte[] buff = new byte[1024];
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(downloadFile));
            int i = bis.read(buff);
            while (i != -1) {
                outputStream.write(buff, 0, buff.length);
                outputStream.flush();
                i = bis.read(buff);
            }
        } catch (IOException e ) {
            e.printStackTrace();
            throw new BusinessException("001", "下载失败，服务开小差了");
        } finally {
            try {
                if(bis != null)
                    bis.close();
                if(outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void editAndSave(SysFileEntity file) {
        SysFileEntity fileInDb = sysFileDao.findById(file.getId());
        if(null == fileInDb) {
            throw new BusinessException("005", "保存失败，缺少参数");
        }
        fileInDb.setRecognitionContent(file.getRecognitionContent());
        sysFileDao.save(fileInDb);
    }

    @Override
    public void save(SysFileEntity file) {
        UserEntity userInDb;
        if(StringUtils.isEmpty(file.getUid())) {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            if(null == principal) {
                throw new BusinessException("001", "获取用户登录凭证失败，请重新登录");
            }
            userInDb = userDao.findByUsername(principal.toString());
            file.setUid(userInDb.getUid());
        }
        sysFileDao.save(file);
    }

    @Override
    public void setFileStar(Integer fileId) {
        if(null == fileId ) {
            throw new BusinessException("004", "参数异常，缺少文件ID");
        }
        sysFileDao.setFileStar(fileId);
    }

    @Override
    public void cancelFileStar(Integer fileId) {
        if(null == fileId ) {
            throw new BusinessException("004", "参数异常，缺少文件ID");
        }
        sysFileDao.cancelFileStar(fileId);
    }

    @Override
    public void deleteFile(Integer fileId) {
        if(null == fileId ) {
            throw new BusinessException("004", "参数异常，缺少文件ID");
        }
        sysFileDao.deleteById(fileId);
    }

    @Override
    public List<SysFileEntity> getAllUploadFile(String uid) {
        if(org.apache.commons.lang.StringUtils.isEmpty(uid)) {
            throw new BusinessException("004", "参数异常，缺少UID");
        }
        List<SysFileEntity> allFile = sysFileDao.findAllByUid(uid);
        return allFile;
    }

}

package com.xiaosong.myframework.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.service.OcrService;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.business.utils.Base64Util;
import com.xiaosong.myframework.business.utils.SysRandomUtil;
import com.xiaosong.myframework.system.utils.SysHttpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@Service("ocrService")
@Transactional(rollbackFor = Exception.class)
public class OcrServiceImpl extends BaseService implements OcrService {

    private static final Base64.Encoder ENCODER_64 = Base64.getEncoder();

    @Override
    public SysFileEntity RecognizeSingleImageAndSave(String ocrApiUrl, String uid, MultipartFile file) throws IOException {
        SysFileEntity sysFileEntity;
        if(!file.isEmpty()) {
            String fileType = file.getContentType();
            if(StringUtils.isEmpty(fileType) || !fileType.startsWith("image")) {
                throw new BusinessException("003", "文件类型错误");
            }
            // Base64编码
            String imageBase64 = ENCODER_64.encodeToString(file.getBytes()).trim();
            // 删除 \r\n
            imageBase64 = imageBase64.replaceAll("[\\s*\t\n\r]", "");
            // 调用接口获取识别结果
            Object recognitionResult = uploadBase64FileToRecognize(ocrApiUrl, imageBase64);
            // 保存文件到本地
            sysFileEntity = saveFileToLocalServer(file, uid, recognitionResult);
        } else {
            throw new BusinessException("003", "文件不能为空");
        }
        return sysFileEntity;
    }

    @Override
    public SysFileEntity getOcrRecognitionResult(String ocrApiUrl, SysFileEntity file) {
        if(StringUtils.isEmpty(file.getFileContent())) {
            throw new BusinessException("004", "文件内容不能为空");
        }
        if(StringUtils.isEmpty(file.getUid())) {
            throw new BusinessException("004", "未指定用户");
        }
        String base64Str = file.getFileContent();
        if(base64Str.split(",").length > 1) {
            base64Str = base64Str.split(",")[1];
        }
        // 删除 \r\n
        base64Str = base64Str.replaceAll("[\\s*\t\n\r]", "");
        // 调用接口获取识别结果
        Object recognitionResult = uploadBase64FileToRecognize(ocrApiUrl, base64Str);
        // 保存文件到本地
        file.setRecognitionContent(JSON.toJSONString(recognitionResult));
        saveBase64FileToLocalServer(base64Str, file);
        return file;
    }

    private Object uploadBase64FileToRecognize(String ocrApiUrl, String base64Str) {
        HashMap<String, Object> requestBody = new HashMap<>();
        ArrayList<String> images = new ArrayList<>();
        if(base64Str.split(",").length > 1) {
            base64Str = base64Str.split(",")[1];
        }
        images.add(base64Str);
        requestBody.put("images", images);
        String ocrResult = "";
        try {
            ocrResult = SysHttpUtils.getInstance().sendJsonPost(ocrApiUrl, JSON.toJSONString(requestBody));
        } catch (Exception e) {
            throw new BusinessException("001", "连接失败，请稍后再试");
        }
        JSONObject result = JSON.parseObject(ocrResult);
        return result.getJSONArray("results").get(0);
    }

    private SysFileEntity saveFileToLocalServer(MultipartFile file, String uid, Object recognitionResult) {
        SysFileEntity sysFileEntity = new SysFileEntity();
        sysFileEntity.setUploadTime(new Timestamp(System.currentTimeMillis()));
        sysFileEntity.setUid(uid);
        sysFileEntity.setFileType(file.getContentType());
        sysFileEntity.setFileSize((int) file.getSize());
        sysFileEntity.setFileName(file.getOriginalFilename());
        sysFileEntity.setRecognitionContent(JSON.toJSONString(recognitionResult));
        sysFileEntity.setSourceGroup("ocr");
        sysFileEntity.setStar(false);
        // 判断操作系统类型
        String os = System.getProperty("os.name");
        String fileRootDir;
        String filePath;
        if(os.toLowerCase().startsWith("win")) {
            fileRootDir = "D:"+ File.separator+"ocr-file"+File.separator+"user"+File.separator + uid + File.separator;
            filePath = fileRootDir;
        } else {
            fileRootDir = "/home/msli/wwwapps/ocr-file/user/"+ uid + "/";
            filePath = "https://www.performercn.com/download/" + uid + "/";
        }
        // 校验文件夹是否存在
        File folder = new File(fileRootDir);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // 保存文件
        String oldFileName = file.getOriginalFilename();
        String newFileName;
        if(StringUtils.isEmpty(oldFileName) || oldFileName.lastIndexOf(".") == 0) {
            newFileName = SysRandomUtil.getRandomString(6);
        } else {
            newFileName = oldFileName.substring(0, oldFileName.lastIndexOf(".")) + "-"
                    + SysRandomUtil.getRandomString(6)
                    + oldFileName.substring(oldFileName.lastIndexOf("."));
        }
        try {
            file.transferTo(new File(fileRootDir, newFileName));
            sysFileEntity.setFileContent(filePath + newFileName);
            sysFileDao.save(sysFileEntity);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("001", "识别失败");
        }
        return sysFileEntity;
    }

    private void saveBase64FileToLocalServer(String base64Str, SysFileEntity file) {
        file.setUploadTime(new Timestamp(System.currentTimeMillis()));
        file.setSourceGroup("ocr");
        file.setStar(false);
        // 判断操作系统类型
        String os = System.getProperty("os.name");
        String fileRootDir;
        String filePath;
        if(os.toLowerCase().startsWith("win")) {
            fileRootDir = "D:" + File.separator + "ocr-file" + File.separator
                    + "user" + File.separator + file.getUid() + File.separator;
            filePath = fileRootDir;
        } else {
            fileRootDir = "/home/msli/wwwapps/ocr-file/user/" + file.getUid() + "/";
            filePath = "https://www.performercn.com/download/" + file.getUid() + "/";
        }
        String fileName = file.getFileName();
        if(StringUtils.isEmpty(fileName) || fileName.lastIndexOf(".") == 0) {
            fileName = SysRandomUtil.getRandomString(6);
        } else {
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-"
                    + SysRandomUtil.getRandomString(6)
                    + fileName.substring(fileName.lastIndexOf("."));
        }
        // 保存到本地
        try {
            Base64Util.base64ContentToFile(base64Str, fileRootDir + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("001", "识别失败");
        }
        // 覆盖内容
        file.setFileContent(filePath + fileName);
        sysFileDao.save(file);
    }
}

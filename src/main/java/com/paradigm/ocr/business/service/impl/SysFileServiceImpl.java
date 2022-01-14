package com.paradigm.ocr.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.paradigm.ocr.business.entity.SysFileEntity;
import com.paradigm.ocr.business.exception.BusinessException;
import com.paradigm.ocr.business.service.base.BaseService;
import com.paradigm.ocr.business.utils.Base64Util;
import com.paradigm.ocr.business.utils.EmojiUtils;
import com.paradigm.ocr.business.utils.PdfOperationUtil;
import com.paradigm.ocr.business.utils.SysRandomUtil;
import com.paradigm.ocr.system.utils.SysHttpUtils;
import com.paradigm.ocr.business.entity.UserEntity;
import com.paradigm.ocr.business.repository.SysFileDao;
import com.paradigm.ocr.business.service.SysFileService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        String principal = SecurityUtils.getSubject().getPrincipal().toString();
        if(null == principal) {
            throw new BusinessException("001", "获取用户登录凭证失败，请重新登录");
        }
        if(!uid.equals(principal)) {
            throw new BusinessException("001", "登录异常，请重新登录");
        }
        String os = System.getProperty("os.name");
        String fileRootDir;
        if(os.toLowerCase().startsWith("win")) {
            fileRootDir = "D:"+ File.separator+"ocr-file"+File.separator+"user"+File.separator + uid + File.separator;
        } else {
            fileRootDir = "/home/msli/wwwapps/ocr-file/user/" + uid + "/";
        }
        // 校验文件夹是否存在
        File folder = new File(fileRootDir);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        List<String> fileUrlList = new ArrayList<>();
        List<SysFileEntity> sysFileEntityList = new ArrayList<>();
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
    public void classifyPdfDocument(MultipartFile file, HttpServletResponse response, String ocrApiUrl) throws IOException {
        if (file.isEmpty()) {
            throw new BusinessException("003", "出错啦，文件不存在");
        }
        String originalFileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFileName) || !originalFileName.endsWith(".pdf")) {
            throw new BusinessException("003", "请上传PDF类型的文件");
        }
        HashMap<String, List<Integer>> classifiedFilesMap = new HashMap<>();
        PDDocument pdfDoc = PdfOperationUtil.load(file.getInputStream());
        for(int i=0; i <= pdfDoc.getNumberOfPages(); i++ ) {
            // 构造图片
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
            BufferedImage buffImage = pdfRenderer.renderImage(i);
            // 写入本地
            String fileRootDir = "D:"+ File.separator+"ocr-file"+File.separator+"user"+File.separator + "self" + File.separator;
            ImageIOUtil.writeImage(buffImage, fileRootDir+"test.jpg", 130);
            // 写入文件中
            File tempFile = File.createTempFile("temp", "png");
            ImageIO.write(buffImage, "png", tempFile);
            // base64 编码
            String pageBase64Str = Base64Util.fileToBase64Str(tempFile);
            // 删除 \r\n
            pageBase64Str = pageBase64Str.replaceAll("[\n\r]", "");
            // 构造请求体
            HashMap<String, Object> requestBody = new HashMap<>();
            ArrayList<String> images = new ArrayList<>();
            images.add(pageBase64Str);
            requestBody.put("images", images);
            String ocrResult = SysHttpUtils.getInstance().sendJsonPost(ocrApiUrl, JSON.toJSONString(requestBody));;
//            JSONObject result = JSON.parseObject(ocrResult);
            // 返回解析
            parseResult(ocrResult, classifiedFilesMap, i);
            tempFile.delete();
        }
    }

    private void parseResult(String ocrResult, HashMap<String, List<Integer>> classifiedFilesMap, int pageIndex) {
        JSONArray resultJsonArray = (JSONArray)JSON.parseObject(ocrResult).getJSONArray("results").get(0);
        boolean bookingNumExisted = false;
        if(resultJsonArray != null && resultJsonArray.size() > 1) {
            for(int i=0; i< resultJsonArray.size(); i++) {
                JSONObject item = resultJsonArray.getJSONObject(i);
                String text = item.getString("text");
                if(EmojiUtils.checkBookingNum(text)) {
                    if(classifiedFilesMap.containsKey(text)) {
                        classifiedFilesMap.get(text).add(i);
                    } else {
                        List<Integer> pages = new ArrayList<>();
                        pages.add(pageIndex);
                        classifiedFilesMap.put(text, pages);
                    }
                    bookingNumExisted = true;
                    break;
                }
            }
        }
        if(!bookingNumExisted) {
            throw new BusinessException("003", "未识别出有效的提单号");
        }
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
                if(bis != null) {
                    bis.close();
                }
                if(outputStream != null) {
                    outputStream.close();
                }
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
        SysFileEntity fileEntity = sysFileDao.findById(fileId).get();
        if(fileEntity.getFileContent().startsWith("https://www.performercn.com")) {
            String[] arr = fileEntity.getFileContent().split("/");
            String filePath = "/home/msli/wwwapps/ocr-file/user/"
                    + arr[arr.length - 2] + "/" + arr[arr.length - 1];
            File fileInServer = new File(filePath);
            if(fileInServer.exists()){
                fileInServer.delete();
            }
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

package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.controller.base.BaseController;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.service.SysFileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */
@RestController
@RequestMapping("/api/ocr/file")
@Log4j2
public class SysFileController extends BaseController {

    @Autowired
    SysFileService sysFileService;

    /**
     * pdf 文件单证分类
     * @param file
     * @param response
     * @return 以附件压缩包形式返回分类后的 pdf 文件
     */
    @PostMapping("/classify-pdf")
    public ApiResult classifyPdfDocument(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        sysFileService.classifyPdfDocument(file, response, ocrApiUrl);
        return ApiResult.T("分类成功");
    }

    /**
     * 上传文件到服务器，保存在服务器本地
     */
    @PostMapping("/upload")
    public ApiResult uploadFileToLocalServer(@RequestParam(value="uid") String uid, @RequestParam("file") MultipartFile[] files) {
        return ApiResult.T(sysFileService.uploadFileToLocalServer(uid, files, dnsUrl));
    }

    /**
     * 下载服务器本地的文件
     */
    @GetMapping("/download")
    public ApiResult downLoadFile(@RequestParam(value="fileName") String fileName, @RequestParam(value="uid") String uid, HttpServletResponse response) {
        sysFileService.downloadLocalServerFile(uid, fileName, response);
        return ApiResult.T("下载成功");
    }


    /**
     * 上传单个文件到数据库，以 base64 形式保存
     * @param file
     * @return
     */
    @PostMapping("/upload-single")
    public ApiResult uploadSinglePicture(@RequestBody SysFileEntity file) {
        file.setUploadTime(new Timestamp(System.currentTimeMillis()));
        sysFileService.save(file);
        return ApiResult.T("上传成功");
    }

    /**
     * 获取指定用户所有的文件
     * @param uid
     * @return
     */
    @GetMapping("/get-all/{uid}")
    public ApiResult getAllUploadFile(@PathVariable String uid) {
        return ApiResult.T(sysFileService.getAllUploadFile(uid));
    }

    /**
     * 收藏指定文件
     * @param fileId
     * @return
     */
    @GetMapping("/set-star/{fileId}")
    public ApiResult setFileStar(@PathVariable Integer fileId) {
        sysFileService.setFileStar(fileId);
        return ApiResult.T();
    }

    /**
     * 取消收藏指定文件
     * @param fileId
     * @return
     */
    @GetMapping("/cancel-star/{fileId}")
    public ApiResult cancelFileStar(@PathVariable Integer fileId) {
        sysFileService.cancelFileStar(fileId);
        return ApiResult.T();
    }

    /**
     * 删除指定文件
     * @param fileId
     * @return
     */
    @GetMapping("/delete/{fileId}")
    public ApiResult deleteFile(@PathVariable Integer fileId) {
        sysFileService.deleteFile(fileId);
        return ApiResult.T();
    }
}

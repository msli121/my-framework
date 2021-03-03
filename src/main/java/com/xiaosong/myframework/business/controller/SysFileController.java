package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.service.SysFileService;
import com.xiaosong.myframework.system.utils.SysStringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */
@RestController
@RequestMapping("/api/file")
@Log4j2
public class SysFileController {

    // 设置固定的日期格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    SysFileService sysFileService;



    /**
     * 上传单个文件
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

    @GetMapping("/delete/{fileId}")
    public ApiResult deleteFile(@PathVariable Integer fileId) {
        sysFileService.deleteFile(fileId);
        return ApiResult.T();
    }
}

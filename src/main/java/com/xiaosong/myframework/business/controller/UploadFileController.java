package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
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
import java.util.UUID;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */
@RestController
@RequestMapping("/api/file")
@Log4j2
public class UploadFileController {

    @Autowired
    SysFileService sysFileService;

    @PostMapping("/upload-single")
    public ApiResult uploadSinglePicture(@RequestBody SysFileEntity file) {
        file.setUploadTime(new Timestamp(System.currentTimeMillis()));
        sysFileService.save(file);
        return ApiResult.T("上传成功");
    }

    @GetMapping("/get-all")
    public ApiResult getAllUploadFile() {
        return ApiResult.T(sysFileService.getAllUploadFile());
    }
}

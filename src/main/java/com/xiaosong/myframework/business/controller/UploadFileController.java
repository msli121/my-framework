package com.xiaosong.myframework.business.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/04
 */
@RestController
public class UploadFileController {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @PostMapping("/upload")
    public String uploadFile(MultipartFile uploadFile, HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath("/uploadFile");
        String format = simpleDateFormat.format(new Date());
        File folder = new File(realPath + format);
        if(!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = uploadFile.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            uploadFile.transferTo(new File(folder, newName));
            String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/uploadFile/" + format + newName;
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }
}

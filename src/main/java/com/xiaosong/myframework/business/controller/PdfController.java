package com.xiaosong.myframework.business.controller;

import com.xiaosong.myframework.business.controller.base.BaseController;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.service.PdfService;
import com.xiaosong.myframework.business.service.SysFileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@RestController
@RequestMapping("/api/ocr/pdf")
@Log4j2
public class PdfController extends BaseController {

    @Autowired
    SysFileService sysFileService;

    @Autowired
    PdfService pdfService;

    /**
     * 识别上传的PDF文件的前5页
     * @param uid
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/recognition/five-page")
    public ApiResult recognisePdfFivePage(@RequestParam(value="uid") String uid, @RequestParam("file") MultipartFile file) throws IOException {
        return ApiResult.T(pdfService.recognisePdfFivePage(file, uid, pdfApiUrl));
    }

    @PostMapping("/upload-single/base64")
    public ApiResult recogniseBase64Pdf(@RequestBody SysFileEntity file){
        return ApiResult.T(pdfService.recogniseBase64Pdf(file, pdfApiUrl));
    }

    /**
     * 识别上传的PDF文件的前5页 url  需要登录
     * @param map
     * @return
     * @throws IOException
     */
    @PostMapping("/recognition/five-page-url")
    public ApiResult recogniseUrlPdfFivePage(@RequestBody Map map) throws IOException {
        return ApiResult.T(pdfService.recogniseUrlPdfFivePage(map, pdfApiUrl));
    }

    /**
     * 识别上传的PDF文件的前5页 url  无需登录
     * @param pdfUrl
     * @return
     * @throws IOException
     */
    @PostMapping("/recognition/five-page-url/direct")
    public ApiResult recogniseUrlPdfFivePageWithoutLogin(@RequestBody String pdfUrl) throws IOException {
        return ApiResult.T(pdfService.recogniseUrlPdfFivePageWithoutLogin(pdfUrl, pdfApiUrl));
    }

    @PostMapping("/edit-save")
    public ApiResult editAndSaveRecognitionResult(@RequestBody SysFileEntity file) {
        sysFileService.editAndSave(file);
        return ApiResult.T("保存成功");
    }

}

package com.xiaosong.myframework.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.service.PdfService;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.business.utils.Base64Util;
import com.xiaosong.myframework.business.utils.PdfOperationUtil;
import com.xiaosong.myframework.system.utils.SysHttpUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/02/28
 */
@Service("pdfService")
@Transactional(rollbackFor = Exception.class)
public class PdfServiceImpl extends BaseService implements PdfService {

    /**
     * 获取 pdf 文件识别结果
     * @param file
     * @param uid
     * @return
     */
    @Override
    public List<HashMap<String, Object>> getOcrRecognitionResult(MultipartFile file, String uid, String pdfApiUrl) throws IOException {
        if(file.isEmpty()) {
            throw new BusinessException("003", "上传文件不能为空");
        }
        String originalFileName = file.getOriginalFilename();
        if(!originalFileName.endsWith(".pdf")) {
            throw new BusinessException("003", "请上传PDF类型的文件");
        }
        List<HashMap<String, Object>> results = new ArrayList<>();
        int lastIndex = originalFileName.lastIndexOf('.');
        String prefix = "";
        String suffix = "";
        if(lastIndex == -1 || lastIndex == originalFileName.length() - 1) {
            prefix = originalFileName;
            suffix = null;
        } else {
            prefix = originalFileName.substring(0, lastIndex);
            suffix = originalFileName.substring(lastIndex + 1);
        }
        PDDocument pdfDoc = PdfOperationUtil.load(file.getInputStream());
        List<PDPage> pdfPageList = PdfOperationUtil.getPageList(pdfDoc);
        for(int i=0; i < pdfPageList.size(); i++) {
            // 初始只识别前5页
            if(i < 5) {
                HashMap<String, Object> item = new HashMap<>();
                File tempFile = File.createTempFile(prefix, suffix);
                PDDocument document = new PDDocument();
                document.addPage(pdfDoc.getPage(i));
                document.save(tempFile);
                PdfOperationUtil.close(document);
                String pageBase64Str = Base64Util.fileToBase64Str(tempFile);
                // 构造请求体
                HashMap<String, Object> requestBody = new HashMap<>();
                requestBody.put("basestr", pageBase64Str);
                String ocrResult = SysHttpUtils.getInstance().sendJsonPost(pdfApiUrl, JSON.toJSONString(requestBody));
                JSONObject pageResult = (JSONObject)((JSONArray)JSON.parse(JSON.parse(ocrResult).toString())).get(0);
                item.put("pageNum", i + 1);
                item.put("data", pageResult.get("data"));
                item.put("shape", pageResult.get("shape"));
                results.add(item);
                tempFile.delete();
            }
        }
        return results;
    }

}

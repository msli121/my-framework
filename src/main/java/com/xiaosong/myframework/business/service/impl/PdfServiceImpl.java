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
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author msli
 * @Date 2021/02/28
 */
@Service("pdfService")
@Transactional(rollbackFor = Exception.class)
public class PdfServiceImpl extends BaseService implements PdfService {

    /**
     * 获取 pdf 文件前 5 页识别结果
     *
     * @param file
     * @param uid
     * @return
     */
    @Override
    public List<HashMap<String, Object>> recognisePdfFivePage(MultipartFile file, String uid, String pdfApiUrl) throws IOException {
        if (file.isEmpty()) {
            throw new BusinessException("003", "上传文件不能为空");
        }
        String originalFileName = file.getOriginalFilename();
        if (!originalFileName.endsWith(".pdf")) {
            throw new BusinessException("003", "请上传PDF类型的文件");
        }
        PDDocument pdfDoc = PdfOperationUtil.load(file.getInputStream());
        List<HashMap<String, Object>> results = getOriginalFivePageRecognitionResult(pdfDoc, 5, pdfApiUrl);
        PdfOperationUtil.close(pdfDoc);
        return results;
    }

    /**
     * 获取 pdf 文件前 5 页识别结果, url pdf
     *
     * @param map
     * @return
     */
    @Override
    public List<HashMap<String, Object>> recogniseUrlPdfFivePage(Map map, String pdfApiUrl) throws IOException {
        String uid = MapUtils.getString(map, "uid");
        String pdfUrl = MapUtils.getString(map, "pdfUrl");
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(pdfUrl)) {
            throw new BusinessException("003", "参数异常，缺少 uid 或 pdfUrl");
        }
        // 通过 url 获取文件
        PDDocument pdfDoc = readPdfFromUrl(pdfUrl);
        List<HashMap<String, Object>> results = getOriginalFivePageRecognitionResult(pdfDoc, 5, pdfApiUrl);
        PdfOperationUtil.close(pdfDoc);
        return results;
    }

    @Override
    public List<HashMap<String, Object>> recogniseUrlPdfFivePageWithoutLogin(String pdfUrl, String pdfApiUrl) throws IOException {
        if (StringUtils.isEmpty(pdfUrl)) {
            throw new BusinessException("003", "参数异常，缺少文件链接");
        }
        // 通过 url 获取文件
        PDDocument pdfDoc = readPdfFromUrl(pdfUrl);
        List<HashMap<String, Object>> results = getOriginalFivePageRecognitionResult(pdfDoc, 5, pdfApiUrl);
        PdfOperationUtil.close(pdfDoc);
        return results;
    }


    /**
     * 返回前 n 页识别结果
     * @param pdfDoc
     * @param pdfApiUrl
     * @return
     * @throws IOException
     */
    public List<HashMap<String, Object>> getOriginalFivePageRecognitionResult(PDDocument pdfDoc, int pageNum, String pdfApiUrl) throws IOException {
        List<HashMap<String, Object>> results = new ArrayList<>();
        List<PDPage> pdfPageList = PdfOperationUtil.getPageList(pdfDoc);
        for (int i = 0; i < pdfPageList.size(); i++) {
            // 识别前 pageNum 页
            if (i < pageNum) {
                HashMap<String, Object> item = new HashMap<>();
                File tempFile = File.createTempFile("temp", "pdf");
                PDDocument document = new PDDocument();
                document.addPage(pdfDoc.getPage(i));
                document.save(tempFile);
                PdfOperationUtil.close(document);
                String pageBase64Str = Base64Util.fileToBase64Str(tempFile);
                // 构造请求体
                HashMap<String, Object> requestBody = new HashMap<>();
                requestBody.put("basestr", pageBase64Str);
                String ocrResult = SysHttpUtils.getInstance().sendJsonPost(pdfApiUrl, JSON.toJSONString(requestBody));
                JSONObject pageResult = (JSONObject) ((JSONArray) JSON.parse(JSON.parse(ocrResult).toString())).get(0);
                item.put("pageNum", i + 1);
                item.put("data", pageResult.get("data"));
                item.put("shape", pageResult.get("shape"));
                results.add(item);
                tempFile.delete();
            }
        }
        return results;
    }

    /**
     * 通过 URL 获得 PDF 内容
     * @param pdfUrl
     * @return
     * @throws Exception
     */
    public PDDocument readPdfFromUrl(String pdfUrl) throws IOException {
        // 是否排序
        boolean sort = false;
        // 开始提取页数
        int startPage = 1;
        // 内存中存储的PDF Document
        PDDocument pdDocument = null;
        //输入流
        InputStream inputStream = null;
        try {
            // 当作一个URL来装载文件
            URL url = new URL(pdfUrl);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(3 * 1000);
            inputStream = con.getInputStream();
            pdDocument = PDDocument.load(inputStream);
        } catch (MalformedURLException e) {
            throw new BusinessException("001", "无效url获取pdf文件失败");
        } finally {
            if (inputStream != null) {
                // 关闭输出流
                inputStream.close();
            }
            if (pdDocument == null) {
                // 获取 PDF Document
                throw new BusinessException("001", "无效url获取pdf文件失败");
            }
        }
        return pdDocument;
    }
}

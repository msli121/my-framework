package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.service.OcrService;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.system.utils.SysHttpUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@Service("ocrService")
@Transactional(rollbackFor = Exception.class)
public class OcrServiceImpl extends BaseService implements OcrService {

    @Override
    public String getOcrRecognitionResult(String url, String jsonData) {
        return SysHttpUtils.getInstance().sendJsonPost(url, jsonData);
    }
}

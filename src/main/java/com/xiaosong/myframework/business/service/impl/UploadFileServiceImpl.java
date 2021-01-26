package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.repository.UploadFileDao;
import com.xiaosong.myframework.business.service.UploadFileService;
import com.xiaosong.myframework.business.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */
@Service("uploadFileService")
@Transactional(rollbackFor = Exception.class)
public class UploadFileServiceImpl extends BaseService implements UploadFileService {

    @Autowired
    public UploadFileDao uploadFileDao;

    @Override
    public void save(SysFileEntity file) {
        uploadFileDao.save(file);
    }
}

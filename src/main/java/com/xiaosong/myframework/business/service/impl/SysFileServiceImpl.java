package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.repository.SysFileDao;
import com.xiaosong.myframework.business.service.SysFileService;
import com.xiaosong.myframework.business.service.base.BaseService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;

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

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
        if(StringUtils.isEmpty(file.getUid())) {
            String username = SecurityUtils.getSubject().getPrincipal().toString();
            UserEntity user = userDao.findByUsername(username);
            file.setUid(user.getUid());
        }
        sysFileDao.save(file);
    }

    @Override
    public List<SysFileEntity> getAllUploadFile() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(null == principal) {
            throw new BusinessException("001", "系统异常，无法获取获取用户登陆凭证");
        }
        UserEntity userInDb = userDao.findByUsername(principal.toString());
        if(null == userInDb) {
            throw new BusinessException("002", "登陆异常，请重新登录");
        }
        List<SysFileEntity> allFile = sysFileDao.findAllByUid(userInDb.getUid());
        return allFile;
    }

}

package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.repository.SysFileDao;
import com.xiaosong.myframework.business.service.SysFileService;
import com.xiaosong.myframework.business.service.base.BaseService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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
    public void save(SysFileEntity file) {
        if(StringUtils.isEmpty(file.getUid())) {
            String username = SecurityUtils.getSubject().getPrincipal().toString();
            UserEntity user = userDao.findByUsername(username);
            file.setUid(user.getUid());
        }
        sysFileDao.save(file);
    }


}

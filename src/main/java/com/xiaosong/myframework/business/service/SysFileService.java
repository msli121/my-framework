package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */

public interface SysFileService {

    void editAndSave(SysFileEntity file);

    void save(SysFileEntity file);

    List<SysFileEntity> getAllUploadFile();
}

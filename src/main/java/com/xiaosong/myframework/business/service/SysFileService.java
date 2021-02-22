package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */

public interface SysFileService {
    void save(SysFileEntity file);
}

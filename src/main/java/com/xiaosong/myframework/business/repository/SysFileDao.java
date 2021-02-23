package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.RolePermissionEntity;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */

public interface SysFileDao extends JpaRepository<SysFileEntity, Integer> {
    SysFileEntity findById(int id);

    List<SysFileEntity> findAllByUid(String uid);
}

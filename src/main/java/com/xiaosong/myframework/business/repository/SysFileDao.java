package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.RolePermissionEntity;
import com.xiaosong.myframework.business.entity.SysFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */

public interface SysFileDao extends JpaRepository<SysFileEntity, Integer> {
    SysFileEntity findById(int id);

    List<SysFileEntity> findAllByUid(String uid);

    @Modifying
    @Query(value = "update t_sys_file set star = 1 where id=?1", nativeQuery = true)
    void setFileStar(Integer fileId);

    @Modifying
    @Query(value = "update t_sys_file set star = 0 where id=?1", nativeQuery = true)
    void cancelFileStar(Integer fileId);

    void deleteById(Integer fileId);
}

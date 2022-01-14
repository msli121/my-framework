package com.paradigm.ocr.business.repository;

import com.paradigm.ocr.business.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/18
 */

public interface PermissionDao extends JpaRepository<PermissionEntity, Integer> {

    @Query("select distinct t.url from PermissionEntity t where t.permissionCode in ?1")
    List<String> getAllPermissionUrlByPermissionCode(List<String> permissionCode);

    @Query("select t from PermissionEntity t where t.permissionCode in ?1")
    List<PermissionEntity> getAllPermissionByPermissionCodes(List<String> permissionCodes);

    List<PermissionEntity> findAllByParentPermissionCode(String parentPermissionCode);

    PermissionEntity findByPermissionCode(String permissionCode);
}

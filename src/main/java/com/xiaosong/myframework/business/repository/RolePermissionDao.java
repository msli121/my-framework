package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.PermissionEntity;
import com.xiaosong.myframework.business.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/18
 */

public interface RolePermissionDao extends JpaRepository<RolePermissionEntity, Integer> {

    @Query("select distinct t.permissionCode from RolePermissionEntity t where t.roleCode in ?1")
    List<String> getAllPermissionCodeByRoleCodes(List<String> roleCodeList);

    @Query("select distinct t.permissionCode from RolePermissionEntity t where t.roleCode = ?1")
    List<String> getAllPermissionCodeByRoleCode(String roleCode);

    void deleteAllByRoleCode(String roleCode);
}

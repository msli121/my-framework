package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.RoleMenuEntity;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleMenuDao extends JpaRepository<RoleMenuEntity, Integer> {

    List<RoleMenuEntity> findAllByRoleCode(String roleCode);

    @Query("select distinct t.menuCode from RoleMenuEntity t where t.roleCode in ?1")
    List<String> getAllMenuCodeByRoleCodes(List<String> roleCodeList);

    @Query("select distinct t.menuCode from RoleMenuEntity t where t.roleCode = ?1")
    List<String> getAllMenuCodeByRoleCode(String roleCode);

    void deleteAllByRoleCode(String roleCode);

}

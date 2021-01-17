package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    List<RoleEntity> findAllByRoleCode(String roleCode);
    List<RoleEntity> findByRoleCodeIn(List<String> roleCodeList);
}

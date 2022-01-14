package com.paradigm.ocr.business.repository;

import com.paradigm.ocr.business.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {

    List<RoleEntity> findAllByRoleCode(String roleCode);

    List<RoleEntity> findByRoleCodeIn(List<String> roleCodeList);

    RoleEntity findByRoleCode(String roleCode);

}

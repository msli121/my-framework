package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.RoleMenuEntity;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleMenuDao extends JpaRepository<RoleMenuEntity, Integer> {

    List<RoleMenuEntity> findAllByRoleId(int roleId);

    @Query("select t.menuId from RoleMenuEntity t where t.roleId in ?1")
    List<Integer> findMenuIdsByRoleIds(List<Integer> roleIds);

    void deleteAllByRoleId(int roleId);

}

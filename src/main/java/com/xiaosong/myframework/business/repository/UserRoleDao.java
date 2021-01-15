package com.xiaosong.myframework.business.repository;

import com.xiaosong.myframework.business.entity.Book;
import com.xiaosong.myframework.business.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleDao extends JpaRepository<UserRoleEntity, Integer> {

    List<UserRoleEntity> findAllByUserId(int userId);

    void deleteAllByUserId(int userId);

    @Query(value = "select distinct role_id from t_user_role where user_id = ?1", nativeQuery = true)
    List<Integer> getAllRoleIdByUserId(Integer userId);


}

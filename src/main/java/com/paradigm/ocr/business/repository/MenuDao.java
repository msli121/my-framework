package com.paradigm.ocr.business.repository;

import com.paradigm.ocr.business.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDao extends JpaRepository<MenuEntity, Integer> {

    MenuEntity findById(int id);

    List<MenuEntity> findAllByParentMenuCode(String parentMenuCode);

    List<MenuEntity> findByMenuCodeIn(List<String> menuCodes);

    MenuEntity findByMenuCode(String menuCode);

}

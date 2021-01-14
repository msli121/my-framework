package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.entity.MenuEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service("menuService")
public class MenuService extends BaseService{
    @Autowired
    public UserService userService;

//    public List<MenuEntity> getMenusFromCurrentUser() {
//        String username = SecurityUtils.getSubject().getPrincipal().toString();
//        UserEntity user = userDao.findByUsername(username);
//
//        List<Integer> roleId =
//    }
}

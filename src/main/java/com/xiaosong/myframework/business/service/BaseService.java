package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.repository.MenuDao;
import com.xiaosong.myframework.business.repository.RoleMenuDao;
import com.xiaosong.myframework.business.repository.UserDao;
import com.xiaosong.myframework.business.repository.UserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected RoleMenuDao roleMenuDao;

    @Autowired
    protected UserRoleDao userRoleDao;

}

package com.xiaosong.myframework.business.service;

import com.xiaosong.myframework.business.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected RoleDao roleDao;

    @Autowired
    protected RoleMenuDao roleMenuDao;

    @Autowired
    protected UserRoleDao userRoleDao;

}

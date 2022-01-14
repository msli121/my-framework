package com.paradigm.ocr.business.service.base;

import com.paradigm.ocr.business.repository.*;
import com.paradigm.ocr.business.repository.*;
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

    @Autowired
    protected PermissionDao permissionDao;

    @Autowired
    protected RolePermissionDao rolePermissionDao;

    @Autowired
    protected SysFileDao sysFileDao;

}

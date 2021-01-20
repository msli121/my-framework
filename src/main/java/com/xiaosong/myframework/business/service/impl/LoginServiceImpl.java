package com.xiaosong.myframework.business.service.impl;

import com.xiaosong.myframework.business.service.LoginService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/20
 */
@Service("login")
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class LoginServiceImpl implements LoginService {

}

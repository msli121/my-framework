package com.xiaosong.myframework.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaosong.myframework.business.constant.BusinessConstant;
import com.xiaosong.myframework.business.dto.ApiResult;
import com.xiaosong.myframework.business.dto.UserDtoEntity;
import com.xiaosong.myframework.business.dto.WeChatLoginDtoEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import com.xiaosong.myframework.business.exception.BusinessException;
import com.xiaosong.myframework.business.response.UserProfileEntity;
import com.xiaosong.myframework.business.service.LoginService;
import com.xiaosong.myframework.business.service.UserService;
import com.xiaosong.myframework.business.service.base.BaseService;
import com.xiaosong.myframework.system.utils.SysHttpUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
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
@Log4j2
public class LoginServiceImpl extends BaseService implements LoginService {

    @Autowired
    UserService userService;

    private boolean checkWeChatWebDtoParams(WeChatLoginDtoEntity loginDtoEntity) {
        boolean checked = true;
        if(StringUtils.isEmpty(loginDtoEntity.getAppId()) || StringUtils.isEmpty(loginDtoEntity.getCode())) {
            checked = false;
        }
        return checked;
    }

    @Override
    public UserProfileEntity login(UserEntity user, String userType) {
        Subject subject = SecurityUtils.getSubject();
        // 设置会话过期时间 默认30分钟
        // subject.getSession().setTimeout(1800000);
        UsernamePasswordToken token = null;
        UserEntity userInDB = null;
        String principal = null;
        if(userType.equals(BusinessConstant.USER_TYPE_WE_CHAT)) {
            // 通过 weChat 登录
            // 使用 openId 和 系统默认密码 生成 token
            token = new UsernamePasswordToken(user.getOpenId(), BusinessConstant.WE_CHAT_USER_DEFAULT_PASSWORD);
            userInDB = userService.findEntityByOpenId(user.getOpenId());
            principal = user.getOpenId();
        } else {
            // 通过用户名密码登录
            token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            userInDB = userService.findEntityByUsername(user.getUsername());
            principal = user.getUsername();
        }
        if(userInDB == null) {
            log.info("用户 [ " + principal + " ] 不存在");
            throw new BusinessException("", "该用户不存在，请先注册");
        }
        if("1".equals(userInDB.getLocked())) {
            log.info("用户 [ " + principal + " ] 已经被禁用");
            throw new BusinessException("", "该用户已被禁用");
        }
        token.setRememberMe(true);
        try {
            subject.login(token);
            // TODO 获取用户角色信息
            return new UserProfileEntity(userInDB);
        } catch (AuthenticationException e) {
            log.info("用户 [ " + principal + " ] 登录失败，请重新登录");
            throw new BusinessException("", "登录失败，请重新登录");
        }
    }

    @Override
    public void passwordRegistry(UserEntity user) {
        boolean exist = userService.isExist(user.getUsername());
        if (exist) {
            throw new BusinessException("", "用户名已被使用");
        }
        // 初始化用户类别
        user.setUserType(BusinessConstant.USER_TYPE_PASSWORD);
        // 用户信息初始化
        userService.initNewUser(user.getPassword(), user);
        userService.save(user);
    }

    @Override
    public UserEntity getUserInfoByWeChat(WeChatLoginDtoEntity loginDtoEntity) {
        if(!checkWeChatWebDtoParams(loginDtoEntity)) {
            throw new BusinessException("", "参数不完整，缺少appId或code");
        }
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + loginDtoEntity.getAppId() +
                "&secret=" + BusinessConstant.WE_CHAT_APP_SECRET +
                "&code=" + loginDtoEntity.getCode() +
                "&grant_type=authorization_code";
        String accessTokenResult = SysHttpUtils.getInstance().sendHttpGet(accessTokenUrl);
        JSONObject jsonResult = JSONObject.parseObject(accessTokenResult);
        String accessToken = jsonResult.getString("access_token");
        String openId = jsonResult.getString("openid");
        String unionId = jsonResult.getString("unionId");
        if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(openId)) {
            throw new BusinessException("001", "获取accessToken失败，请重新扫码");
        }
        UserEntity user = null;
        user = userDao.findByOpenId(openId);
        if(null == user && StringUtils.isNotEmpty(unionId)) {
            user = userDao.findByUnionId(unionId);
        }
        if(null == user) {
            // 未查询到用户，新增用户
            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=" + accessToken +
                    "&openid=" + openId;
            JSONObject userInfo = JSON.parseObject(SysHttpUtils.getInstance().sendHttpGet(userInfoUrl));
            if(StringUtils.isNotEmpty(userInfo.getString("errcode"))) {
                throw new BusinessException("", userInfo.getString("errmsg"));
            }
            user = new UserEntity();
            user.setUsername(userInfo.getString("nickname"));
            user.setOpenId(openId);
            user.setSex(userInfo.getString("sex"));
            user.setCountry(userInfo.getString("country"));
            user.setProvince(userInfo.getString("province"));
            user.setCity(userInfo.getString("city"));
            user.setHeadImgUrl(userInfo.getString("headimgurl"));
            user.setAvatar(userInfo.getString("headimgurl"));
            user.setUnionId(userInfo.getString("unionid"));
            user.setUserType(BusinessConstant.USER_TYPE_WE_CHAT);
            // 用户信息初始化
            userService.initNewUser(BusinessConstant.WE_CHAT_USER_DEFAULT_PASSWORD, user);
            userDao.save(user);
            // TODO 给新用户增加默认角色

        }
        return user;
    }
}

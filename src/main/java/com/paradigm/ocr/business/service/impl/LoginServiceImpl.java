package com.paradigm.ocr.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paradigm.ocr.business.constant.BusinessConstant;
import com.paradigm.ocr.business.dto.WeChatLoginDtoEntity;
import com.paradigm.ocr.business.exception.BusinessException;
import com.paradigm.ocr.business.response.UserProfileEntity;
import com.paradigm.ocr.business.service.base.BaseService;
import com.paradigm.ocr.business.utils.EmojiUtils;
import com.paradigm.ocr.system.utils.SysHttpUtils;
import com.paradigm.ocr.business.entity.UserEntity;
import com.paradigm.ocr.business.service.LoginService;
import com.paradigm.ocr.business.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

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
    public UserProfileEntity login(UserEntity requestUser, String userType) {
        Subject subject = SecurityUtils.getSubject();
        // 设置会话过期时间 默认30分钟
        // subject.getSession().setTimeout(1800000);
        UsernamePasswordToken token;
        UserEntity userInDB = null;
        if(userType.equals(BusinessConstant.USER_TYPE_WE_CHAT)) {
            // weChat 登录，通过 openId 查询用户
            userInDB = userService.findEntityByOpenId(requestUser.getOpenId());
        } else {
            // 密码登录，通过用户名或邮箱查询用户
            if(StringUtils.isNotEmpty(requestUser.getUsername())) {
                userInDB = userService.findUserByUsername(requestUser.getUsername());
            }
            if(null != userInDB && StringUtils.isNotEmpty(requestUser.getEmail())) {
                userInDB = userService.findUserByEmail(requestUser.getEmail());
            }
        }
        if(userInDB == null) {
            log.info("用户不存在");
            throw new BusinessException("003", "该用户不存在，请先注册");
        }
        if("1".equals(userInDB.getLocked())) {
            log.info("用户已经被禁用");
            throw new BusinessException("003", "该用户已被禁用");
        }
        if(userType.equals(BusinessConstant.USER_TYPE_WE_CHAT)) {
            // 使用 uid 和 微信用户默认密码 生成 token
            token = new UsernamePasswordToken(userInDB.getUid(), BusinessConstant.WE_CHAT_USER_DEFAULT_PASSWORD);
        } else {
            // 使用 uid 和 密码 生成 token
            token = new UsernamePasswordToken(userInDB.getUid(), requestUser.getPassword());
        }
        token.setRememberMe(true);
        try {
            subject.login(token);
            // TODO 获取用户角色信息
            return new UserProfileEntity(userInDB);
        } catch (AuthenticationException e) {
            log.info("用户 [ " + userInDB.getUid() + " ] 登录失败，请重新登录");
            throw new BusinessException("", "登录失败，请重新登录");
        } catch (UnsupportedEncodingException e) {
            log.info("系统异常，解码昵称失败");
            throw new BusinessException("", "登录失败，请重新登录");
        }
    }

    @Override
    public void passwordRegistry(UserEntity user) {
        boolean userNameExist = false;
        boolean emailExist = false;
        if(StringUtils.isNotEmpty(user.getUsername())) {
            userNameExist = userService.checkExistByUsername(user.getUsername());
        }
        if(StringUtils.isNotEmpty(user.getEmail())) {
            emailExist = userService.checkExistByEmail(user.getEmail());
        }
        if(userNameExist) {
            throw new BusinessException("003", "用户名已被使用");
        }
        if(emailExist) {
            throw new BusinessException("003", "邮箱已被注册");
        }
        // 初始化用户类别
        user.setUserType(BusinessConstant.USER_TYPE_PASSWORD);
        // 用户信息初始化
        userService.initNewUser(user.getPassword(), user);
        userService.save(user);
    }

    @Override
    public UserEntity getUserInfoByWeChat(WeChatLoginDtoEntity loginDtoEntity) throws UnsupportedEncodingException {
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
        String unionId = jsonResult.getString("unionid");
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
            // 生成用户唯一标识 UID
            user.setUid(userService.generateUid());
            // 设置昵称
            String nickname = userInfo.getString("nickname");
            if(EmojiUtils.checkUsername(nickname)) {
                user.setUsername(nickname);
            } else {
                // 存储 base64 编码后的字符串
                String base64Nickname = Base64.encodeBase64String(nickname.getBytes(StandardCharsets.UTF_8));
                user.setUsername(base64Nickname);
                user.setHasEmoji(true);
            }
            user.setOpenId(openId);
            user.setSex(userInfo.getString("sex").equals("1") ? "male" : userInfo.getString("sex").equals("2") ? "female" : "x");
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

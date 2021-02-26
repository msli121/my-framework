package com.xiaosong.myframework.business.response;

import com.xiaosong.myframework.business.entity.UserEntity;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.BooleanUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Data
public class UserProfileEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String uid;
    private String username;
    private String birthday;
    private String avatar;
    private String phone;
    private String sysHeadIcon;
    private String country;
    private String province;
    private String city;
    private String organization;
    private String sex;
    private String email;
    private String userType;
    // 微信相关
    private String openId;
    private String unionId;
    private String headImgUrl;

    public UserProfileEntity(UserEntity user) throws UnsupportedEncodingException {
        this.id = user.getId();
        this.uid = user.getUid();
        this.username = BooleanUtils.isTrue(user.getHasEmoji()) ? new String(Base64.decodeBase64(user.getUsername()), StandardCharsets.UTF_8) : user.getUsername();
        this.avatar = user.getAvatar();
        this.birthday = user.getBirthday();
        this.sysHeadIcon = user.getSysHeadIcon();
        this.phone = user.getPhone();
        this.country = user.getCountry();
        this.province = user.getProvince();
        this.city = user.getCity();
        this.organization = user.getOrganization();
        this.sex = user.getSex();
        this.email = user.getEmail();
        this.userType = user.getUserType();
        this.openId = user.getOpenId();
        this.unionId = user.getUnionId();
        this.headImgUrl = user.getHeadImgUrl();
    }
}

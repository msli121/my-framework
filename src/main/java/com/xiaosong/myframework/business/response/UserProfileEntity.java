package com.xiaosong.myframework.business.response;

import com.xiaosong.myframework.business.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserProfileEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String phone;
    private String sysHeadIcon;

    public UserProfileEntity(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.sysHeadIcon = user.getSysHeadIcon();
        this.phone = user.getPhone();
    }

    public UserProfileEntity(int id, String username, String sysHeaderIcon) {
        this.id = id;
        this.username = username;
        this.sysHeadIcon = sysHeaderIcon;
    }
}

package com.paradigm.ocr.business.dto;

import com.paradigm.ocr.business.dto.base.OutputConverter;
import com.paradigm.ocr.business.entity.RoleEntity;
import com.paradigm.ocr.business.entity.UserEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserDtoEntity implements OutputConverter<UserDtoEntity, UserEntity> {

    private Integer id;

    private String username;

    private String country;

    private String province;

    private String city;

    private String sex;

    private String sysHeadIcon;

    private String phone;

    private String email;

    private String userType;

    // 微信相关
    private String openId;

    private String unionId;

    private String headImgUrl;

    private String enabled;

    private String locked;

    private List<RoleEntity> roles;
}

package com.xiaosong.myframework.business.dto;

import com.xiaosong.myframework.business.dto.base.OutputConverter;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserDtoEntity implements OutputConverter<UserDtoEntity, UserEntity> {
    private Integer id;

    private String username;

    private String sysHeadIcon;

    private String phone;

    private String email;

    private String enabled;

    private String locked;

    private List<RoleEntity> roles;
}

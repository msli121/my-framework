package com.xiaosong.myframework.business.dto;

import com.xiaosong.myframework.business.dto.base.OutputConverter;
import com.xiaosong.myframework.business.entity.MenuEntity;
import com.xiaosong.myframework.business.entity.PermissionEntity;
import com.xiaosong.myframework.business.entity.RoleEntity;
import com.xiaosong.myframework.business.entity.UserEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RoleDtoEntity implements OutputConverter<RoleDtoEntity, RoleEntity> {
    private Integer id;
    private String roleCode;
    private String roleName;
    private String enabled;
    private String description;
    private List<PermissionEntity> permissions;
    private List<MenuEntity> menus;
}

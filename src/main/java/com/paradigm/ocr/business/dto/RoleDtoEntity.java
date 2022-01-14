package com.paradigm.ocr.business.dto;

import com.paradigm.ocr.business.dto.base.OutputConverter;
import com.paradigm.ocr.business.entity.MenuEntity;
import com.paradigm.ocr.business.entity.PermissionEntity;
import com.paradigm.ocr.business.entity.RoleEntity;
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

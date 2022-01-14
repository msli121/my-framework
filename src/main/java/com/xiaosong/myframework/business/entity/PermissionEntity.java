package com.xiaosong.myframework.business.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_permission", schema = "ocr", catalog = "")
public class PermissionEntity implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    private int id;
    private String groupCode;
    private String permissionCode;
    private String nameZh;
    private String parentPermissionCode;
    private String url;
    private String enabled = "1";
    private String description;

    List<PermissionEntity> children;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "group_code")
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Basic
    @Column(name = "permission_code")
    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "name_zh")
    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    @Basic
    @Column(name = "enabled")
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "parent_permission_code")
    public String getParentPermissionCode() {
        return parentPermissionCode;
    }

    public void setParentPermissionCode(String parentPermissionCode) {
        this.parentPermissionCode = parentPermissionCode;
    }

    @Transient
    public List<PermissionEntity> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionEntity> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionEntity that = (PermissionEntity) o;
        return id == that.id && Objects.equals(groupCode, that.groupCode) && Objects.equals(permissionCode, that.permissionCode) && Objects.equals(url, that.url) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupCode, permissionCode, url, description);
    }
}

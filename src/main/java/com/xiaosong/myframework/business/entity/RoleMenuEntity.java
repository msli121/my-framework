package com.xiaosong.myframework.business.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_role_menu", schema = "xiaosong", catalog = "")
public class RoleMenuEntity {
    private int id;
    private int roleId;
    private int menuId;

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
    @Column(name = "role_id")
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "menu_id")
    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleMenuEntity that = (RoleMenuEntity) o;
        return id == that.id && roleId == that.roleId && menuId == that.menuId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleId, menuId);
    }
}

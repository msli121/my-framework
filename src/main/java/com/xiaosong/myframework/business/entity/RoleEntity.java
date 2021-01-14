package com.xiaosong.myframework.business.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/13
 */

@Entity
@Table(name = "t_role", schema = "xiaosong", catalog = "")
public class RoleEntity implements Serializable {
    private int id;
    private String roleName;
    private String description;

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
    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return id == that.id &&
                Objects.equals(roleName, that.roleName) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, description);
    }
}

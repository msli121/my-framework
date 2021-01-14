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
@Table(name = "t_user", schema = "xiaosong", catalog = "")
public class UserEntity implements Serializable {
    private int id;
    private String username;
    private String password;
    private String salt;
    private Byte enabled = 1;
    private Byte locked = 0;

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
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Basic
    @Column(name = "enabled")
    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "locked")
    public Byte getLocked() {
        return locked;
    }

    public void setLocked(Byte locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(locked, that.locked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, enabled, locked);
    }
}

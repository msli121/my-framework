package com.xiaosong.myframework.business.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_user", schema = "xiaosong", catalog = "")
public class UserEntity implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String salt;
    private String sysHeadIcon;
    private String enabled = "1";
    private String locked = "0";

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
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    @Column(name = "sys_header_icon")
    public String getSysHeadIcon() {
        return sysHeadIcon;
    }

    public void setSysHeadIcon(String sysHeaderIcon) {
        this.sysHeadIcon = sysHeaderIcon;
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
    @Column(name = "locked")
    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(phone, that.phone) && Objects.equals(salt, that.salt) && Objects.equals(enabled, that.enabled) && Objects.equals(locked, that.locked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, phone, salt, enabled, locked);
    }
}

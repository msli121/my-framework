//package com.xiaosong.myframework.business.entity;
//
//import javax.persistence.*;
//
//@Entity(name = "t_role")
//public class Role {
//    private Integer id;
//    private String roleName;
//    private String description;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    @Column(name = "role_name", nullable = false)
//    public String getRoleName() {
//        return roleName;
//    }
//
//    public void setRoleName(String roleName) {
//        this.roleName = roleName;
//    }
//
//    @Column(name = "description", nullable = false)
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//}

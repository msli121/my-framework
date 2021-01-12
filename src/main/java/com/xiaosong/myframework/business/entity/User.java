package com.xiaosong.myframework.business.entity;

import lombok.Data;
import java.util.List;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean locked;
    private List<Role> roles;
}

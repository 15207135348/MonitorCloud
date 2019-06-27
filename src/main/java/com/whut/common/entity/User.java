package com.whut.common.entity;

import javax.persistence.*;

/**
 * 用户实体
 * 设置：用户名、密码类型
 * @author yy
 * @time 2018-03-26
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "code")
    private String code;
    @Column(name = "enabled")
    private boolean enabled;

    public User() {

    }

    public User(String username,
                String password,
                String email,
                boolean enabled,
                String code) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", code='" + code + '\'' +
                '}';
    }
}
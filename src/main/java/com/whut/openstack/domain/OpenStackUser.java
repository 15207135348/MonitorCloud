package com.whut.openstack.domain;

import com.alibaba.fastjson.JSONObject;


/**
 * 用户实体
 * 设置：用户名、密码类型
 * @author yy
 * @time 2018-03-26
 */
public class OpenStackUser {

    private String id;
    private String username;
    private String password;
    private String domain;
    private String team;
    private String description;
    private boolean enabled;

    public OpenStackUser() {

    }

    public OpenStackUser(String username, String password, String description) {
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public OpenStackUser(String username, String password, String domain, String team, String description) {
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.team = team;
        this.description = description;
    }

    public OpenStackUser(String id, String username, String password, String domain, String team, String description, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.team = team;
        this.description = description;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("id",this.id);
        object.put("name",this.username);
        object.put("password",this.password);
        object.put("domain",this.domain);
        object.put("team",this.team);
        object.put("description",description);
        object.put("enabled",enabled);
        return object.toJSONString();
    }
}
package com.whut.openstack.domain;

import java.sql.Timestamp;

public class OpenStackToken {


    private String username;
    private String token;
    private Timestamp expires;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpires() {
        return expires;
    }

    public void setExpires(Timestamp expires) {
        this.expires = expires;
    }
}

package com.whut.openstack.domain;

import com.alibaba.fastjson.JSONObject;


/**
 * Created by YY
 */

public class VirtualHost {

    private String id;
    private String hostname;
    private String system;
    private String image;
    private String flavor;
    private String mac;
    private String ipv4;
    private String created;
    private String status;
    private String team;
    private String user;

    public VirtualHost() {
    }

    public VirtualHost(String team, String user) {
        this.team = team;
        this.user = user;
    }

    public VirtualHost(String hostname, String image, String flavor, String team, String user) {
        this.hostname = hostname;
        this.image = image;
        this.flavor = flavor;
        this.team = team;
        this.user = user;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("id",this.id);
        object.put("name",this.hostname);
        object.put("ip",this.ipv4);
        object.put("status",this.status);
        object.put("time",this.created);
        return object.toJSONString();
    }
}
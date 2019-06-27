package com.whut.monitoring.entity;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

/**
 * @author 杨赟
 * @describe
 * @since 19-1-19 下午9:01
 */
@Entity
@Table(name = "iptables")
public class IPTables {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "ip")
    private String ip;
    @Column(name = "reason")
    private String reason;

    public IPTables() {
    }

    public IPTables(String ip, String reason) {
        this.ip = ip;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public JSONObject toJSON() {

        JSONObject object = new JSONObject();
        object.put("id",id);
        object.put("ip",ip);
        object.put("reason",reason);
        return object;
    }
}

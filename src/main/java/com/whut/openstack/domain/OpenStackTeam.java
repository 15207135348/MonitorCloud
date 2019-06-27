package com.whut.openstack.domain;

import com.alibaba.fastjson.JSONObject;

/**
 * @since 18-11-21 下午9:24
 * @author 杨赟
 * @describe 多个用户组成一个团队
 */

public class OpenStackTeam {

    private String id;
    private String name;
    private String description;

    public OpenStackTeam() {
    }

    public OpenStackTeam(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String toString()
    {
        JSONObject object = new JSONObject();
        object.put("id",this.id);
        object.put("name",this.name);
        object.put("description",this.description);
        return object.toString();
    }
}

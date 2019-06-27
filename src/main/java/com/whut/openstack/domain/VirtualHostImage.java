package com.whut.openstack.domain;

import com.alibaba.fastjson.JSONObject;


public class VirtualHostImage {

    private String id;
    private String name;


    public VirtualHostImage() {
    }

    public VirtualHostImage(String name) {
        this.name = name;
    }

    public VirtualHostImage(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString()
    {
        JSONObject object = new JSONObject();
        object.put("id",this.id);
        object.put("name",this.name);
        return object.toString();
    }
}

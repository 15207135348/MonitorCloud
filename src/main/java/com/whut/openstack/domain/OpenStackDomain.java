package com.whut.openstack.domain;


import com.alibaba.fastjson.JSONObject;

/**
 * @since 18-11-21 下午9:23
 * @author 杨赟
 * @describe OpenStack中的域名
 */

public class OpenStackDomain {

    private String id;
    private String name;
    private String description;
    private boolean enabled;

    public OpenStackDomain(String id, String name, String description, boolean enabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString()
    {
        JSONObject object = new JSONObject();
        object.put("id",this.id);
        object.put("name",this.name);
        object.put("description",this.description);
        object.put("enabled",this.enabled);
        return object.toString();
    }
}

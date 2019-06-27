package com.whut.openstack.domain;


import com.alibaba.fastjson.JSONObject;

public class VirtualHostFlavor {

    private String id;
    private String name;
    private Integer cpu;
    private Integer ram;
    private Integer swap;
    private Integer disk;
    private String description;

    public VirtualHostFlavor() {
    }

    public VirtualHostFlavor(String id, String name, Integer cpu, Integer ram, Integer disk, String description) {
        this.id = id;
        this.name = name;
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
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

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Integer getSwap() {
        return swap;
    }

    public void setSwap(Integer swap) {
        this.swap = swap;
    }

    public Integer getDisk() {
        return disk;
    }

    public void setDisk(Integer disk) {
        this.disk = disk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString()
    {
        JSONObject object = new JSONObject();
        object.put("id",this.id);
        object.put("name",this.name);
        object.put("cpu",this.cpu);
        object.put("ram",this.ram);
        object.put("swap",this.swap);
        object.put("disk",this.disk);
        object.put("description",this.description);
        return object.toString();
    }
}

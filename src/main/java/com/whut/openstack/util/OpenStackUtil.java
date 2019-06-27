package com.whut.openstack.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.whut.common.util.TimeFormatUtil;
import com.whut.openstack.domain.*;
import java.util.ArrayList;
import java.util.List;

public class OpenStackUtil {

    public static OpenStackTeam parseTenant(String string)
    {
        OpenStackTeam team = null;
        try {
            team = new OpenStackTeam();
            JSONObject object = JSONObject.parseObject(string).getJSONObject("project");
            team.setId(object.getString("id"));
            team.setName(object.getString("name"));
            team.setDescription(object.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return team;
    }

    public static List<OpenStackTeam> parseTenants(String string)
    {
        List<OpenStackTeam> teams = new ArrayList<>();;
        try {
            JSONArray array = JSONObject.parseObject(string).getJSONArray("projects");
            for (int i = 0; i < array.size(); i++) {
                OpenStackTeam team = new OpenStackTeam();
                JSONObject object = array.getJSONObject(i);
                team.setId(object.getString("id"));
                team.setName(object.getString("name"));
                team.setDescription(object.getString("description"));
                teams.add(team);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teams;
    }

    public static OpenStackUser parseUser(String string)
    {
        OpenStackUser user = null;
        try {
            user = new OpenStackUser();
            JSONObject object = JSONObject.parseObject(string).getJSONObject("user");
            user.setUsername(object.getString("name"));
            user.setDomain(object.getString("domain_id"));
            user.setEnabled(object.getBoolean("enabled"));
            user.setTeam(object.getString("default_project_id"));
            user.setId(object.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<OpenStackUser> parseUsers(String string)
    {
        List<OpenStackUser> users = new ArrayList<>();
        try {
            JSONArray array = JSONObject.parseObject(string).getJSONArray("users");
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                OpenStackUser user = new OpenStackUser();
                user.setUsername(object.getString("name"));
                user.setDomain(object.getString("domain_id"));
                user.setEnabled(object.getBoolean("enabled"));
                user.setTeam(object.getString("default_project_id"));
                user.setId(object.getString("id"));
                users.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void parseHostInfo(JSONObject server, VirtualHost host)
    {
        try {
            String hostId = server.getString("id");
            String hostname = server.getString("name");
            String status = server.getString("status");
            String flavor = server.getJSONObject("flavor").getString("id");
            String created = server.getString("created");
            created = TimeFormatUtil.stampToTime(TimeFormatUtil.utcStr2Stamp(created,"yyyy-MM-dd'T'HH:mm:ss Z"),"yyyy-MM-dd hh:mm:ss");
            String imageId = server.getJSONObject("image").getString("id");
            host.setId(hostId);
            host.setHostname(hostname);
            host.setStatus(status);
            host.setImage(imageId);
            host.setFlavor(flavor);
            host.setCreated(created);
            JSONArray provider = server.getJSONObject("addresses").getJSONArray("provider");
            if(provider.size()>0)
            {
                String mac = provider.getJSONObject(0).getString("OS-EXT-IPS-MAC:mac_addr");
                String ipv4 = provider.getJSONObject(0).getString("addr");
                host.setMac(mac);
                host.setIpv4(ipv4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static VirtualHostFlavor parseFlavor(String string)
    {
        VirtualHostFlavor flavor= new VirtualHostFlavor();
        try {
            JSONObject object = JSONObject.parseObject(string).getJSONObject("flavor");
            Integer ram = object.getInteger("ram");
            Integer disk = object.getInteger("disk");
            Integer vcpus = object.getInteger("vcpus");
            String name = object.getString("name");
            String flavorId = object.getString("id");
            flavor.setName(name);
            flavor.setId(flavorId);
            flavor.setCpu(vcpus);
            flavor.setRam(ram);
            flavor.setDisk(disk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flavor;
    }

    public static List<VirtualHostFlavor> parseFlavors(String string)
    {
        List<VirtualHostFlavor> list= new ArrayList<>();
        try {

            JSONArray array = JSONObject.parseObject(string).getJSONArray("flavors");

            for (int i = 0; i <array.size() ; i++) {
                VirtualHostFlavor flavor = new VirtualHostFlavor();
                JSONObject object = array.getJSONObject(i);
                Integer ram = object.getInteger("ram");
                Integer disk = object.getInteger("disk");
                Integer vcpus = object.getInteger("vcpus");
                Integer swap =  Integer.parseInt(object.getString("swap"));
                String name = object.getString("name");
                String flavorId = object.getString("id");
                flavor.setName(name);
                flavor.setId(flavorId);
                flavor.setCpu(vcpus);
                flavor.setRam(ram);
                flavor.setSwap(swap);
                flavor.setDisk(disk);
                list.add(flavor);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static VirtualHostImage parseImage(String string)
    {
        VirtualHostImage image= new VirtualHostImage();
        try {
            JSONObject object = JSONObject.parseObject(string).getJSONObject("image");
            String imageId = object.getString("id");
            String imageName = object.getString("name");
            image.setId(imageId);
            image.setName(imageName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static List<VirtualHostImage> parseImages(String string)
    {
        List<VirtualHostImage> images= new ArrayList<>();
        try {
            JSONArray array = JSONObject.parseObject(string).getJSONArray("images");
            for (int i = 0; i <array.size() ; i++) {
                VirtualHostImage image = new VirtualHostImage();
                JSONObject object = array.getJSONObject(i);
                String imageId = object.getString("id");
                String imageName = object.getString("name");
                image.setId(imageId);
                image.setName(imageName);
                images.add(image);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }
}

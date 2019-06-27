package com.whut.openstack.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.whut.common.domain.HPair;
import com.whut.openstack.domain.*;
import com.whut.openstack.service.OpenStackService;
import com.whut.common.util.Base64Util;
import com.whut.common.util.HttpUtil;
import com.whut.openstack.util.OpenStackUtil;
import com.whut.common.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OpenStackServiceImpl implements OpenStackService{

    private static final String CONTROLLER = "59.69.101.206";
    private static final String TOKEN_URL = "http://"+CONTROLLER+":5000/v2.0/tokens";
    private static final String TENANT_URL = "http://"+CONTROLLER+":5000/v3/projects";
    private static final String USER_URL = "http://"+CONTROLLER+":5000/v3/users";
    private static final String COMPUTE_URL = "http://"+CONTROLLER+":8774/v2.1";
    private static final String IMAGE_URL = "http://"+CONTROLLER+":9292/v2/images";
    //超级管理员的用户名
    private static final OpenStackUser ADMIN = new OpenStackUser();

    static {
        ADMIN.setDomain("default");
        ADMIN.setTeam("3db9ca04b5334fcd96bd065d9d789836");
        ADMIN.setId("42f2c49f4e9c401da1fcb9057809e464");
        ADMIN.setUsername("admin");
        ADMIN.setPassword("root");
        ADMIN.setEnabled(true);
        ADMIN.setDescription("超级管理员");
    }

    private Map<String,OpenStackUser> userMap = new HashMap<>();
    private Map<String,OpenStackToken> tokenMap = new HashMap<>();

    private static OpenStackToken newToken(String tenantId, String username, String password) {
        long start = new Date().getTime();

        OpenStackToken token = new OpenStackToken();
        token.setUsername(username);

        JSONObject body = new JSONObject();
        JSONObject auth = new JSONObject();
        JSONObject passwordCredentials = new JSONObject();
        try {
            passwordCredentials.put("username",username);
            passwordCredentials.put("password",password);
            auth.put("tenantId",tenantId);
            auth.put("passwordCredentials",passwordCredentials);
            body.put("auth",auth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HPair<Integer,String> pair = HttpUtil.postByJson(TOKEN_URL,body.toString());
        if(pair.getFirst() == 200)
        {
            try {
                JSONObject object = JSONObject.parseObject(pair.getSecond()).getJSONObject("access").getJSONObject("token");
                String tokenId = object.getString("id");
                String expires = object.getString("expires");
                String issued = object.getString("issued_at");
                long expires_ = TimeFormatUtil.getExpires(issued,expires,start);
                token.setToken(tokenId);
                token.setExpires(new Timestamp(expires_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    private void reloadUserMap(){
        userMap.clear();
        userMap.put(ADMIN.getUsername(),ADMIN);
        List<OpenStackUser> openStackUsers = listUser(adminToken());
        if (openStackUsers == null) return;
        for (OpenStackUser user : openStackUsers){
            userMap.put(user.getUsername(),user);
        }
    }

    @Autowired
    public OpenStackServiceImpl() {
        reloadUserMap();
    }

    @Override
    public OpenStackToken adminToken() {
        return getToken(ADMIN.getUsername());
    }

    @Override
    public OpenStackToken getToken(String username)
    {
        OpenStackToken token = tokenMap.get(username);
        if(token == null || token.getExpires().getTime() < new Date().getTime())
        {
            OpenStackUser openStackUser = userMap.get(username);
            if(openStackUser == null) {
                return null;
            }
            token = newToken(openStackUser.getTeam(),username,openStackUser.getPassword());
            tokenMap.put(username,token);
        }
        return token;
    }

    @Override
    public List<OpenStackTeam> listTenant(OpenStackToken token) {
        List<OpenStackTeam> tenants = null;
        HPair<Integer,String> pair =HttpUtil.getByJsonAndToken(TENANT_URL,token.getToken());
        if (pair.getFirst()==200) {
            tenants = OpenStackUtil.parseTenants(pair.getSecond());
        }
        return tenants;
    }

    @Override
    public List<OpenStackTeam> listTenantOfUser(OpenStackToken token, String userId) {
        List<OpenStackTeam> tenants = null;
        HPair<Integer,String> pair =HttpUtil.getByJsonAndToken(
                TENANT_URL.concat("/").concat(userId).concat("/projects"),
                token.getToken());
        if (pair.getFirst()==200) {
            tenants = OpenStackUtil.parseTenants(pair.getSecond());
        }
        return tenants;
    }

    @Override
    public OpenStackTeam showTenant(OpenStackToken token, String tenantId) {
        OpenStackTeam tenant = null;
        HPair<Integer,String> pair =HttpUtil.getByJsonAndToken(TENANT_URL.concat("/").concat(tenantId),token.getToken());
        if (pair.getFirst()==200) {
            tenant = OpenStackUtil.parseTenant(pair.getSecond());
        }
        return tenant;
    }

    @Override
    public OpenStackTeam createTenant(OpenStackToken token, String tenantName, String description) {

        OpenStackTeam tenant = null;
        JSONObject body = new JSONObject();
        JSONObject project = new JSONObject();
        try {
            project.put("description",description);
            project.put("name",tenantName);
            project.put("domain_id","default");
            project.put("enabled",true);
            project.put("is_domain",false);
            body.put("project",project);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HPair<Integer,String> pair =HttpUtil.postByJsonAndToken(TENANT_URL,token.getToken(),body.toString());
        if(pair.getFirst() == 201) {
            tenant = OpenStackUtil.parseTenant(pair.getSecond());
        }
        return tenant;
    }

    @Override
    public boolean deleteTenant(OpenStackToken token, String tenantId) {
         return HttpUtil.deleteByJsonAndToken(TENANT_URL.concat("/").concat(tenantId),token.getToken()).getFirst() == 204;
    }

    @Override
    public OpenStackUser getUser(String username) {
        if (!userMap.containsKey(username)){
            reloadUserMap();
        }
        return userMap.get(username);
    }

    @Override
    public List<OpenStackUser> listUser(OpenStackToken token) {
        List<OpenStackUser> users = null;
        HPair<Integer,String> pair =HttpUtil.getByJsonAndToken(USER_URL, token.getToken());
        if (pair.getFirst()==200) {
            users = OpenStackUtil.parseUsers(pair.getSecond());
        }
        return users;
    }

    @Override
    public OpenStackUser showUser(OpenStackToken token, String userId) {
        OpenStackUser user = null;
        HPair<Integer,String> pair =HttpUtil.getByJsonAndToken(
                USER_URL.concat("/").concat(userId),
                token.getToken());
        if (pair.getFirst()==200) {
            user = OpenStackUtil.parseUser(pair.getSecond());
        }
        return user;
    }

    @Override
    public OpenStackUser createUser(OpenStackToken token, OpenStackUser user) {

        JSONObject body = new JSONObject();
        JSONObject user_ = new JSONObject();
        try {
            user_.put("description",user.getDescription());
            user_.put("name",user.getUsername());
            user_.put("password",user.getPassword());
            user_.put("domain_id",user.getDomain());
            user_.put("enabled",true);
            user_.put("default_project_id",user.getTeam());
            body.put("user",user_);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HPair<Integer,String> pair =HttpUtil.postByJsonAndToken(USER_URL,token.getToken(),body.toString());
        if(pair.getFirst() == 201) {
            OpenStackUser user__ = OpenStackUtil.parseUser(pair.getSecond());
            user.setId(user__.getId());
            user.setEnabled(user__.isEnabled());
            userMap.put(user.getUsername(),user);
        }
        return user;
    }


    @Override
    public boolean changePassword(OpenStackToken token, String userId, String original, String password) {
        JSONObject body = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            user.put("password",password);
            user.put("original_password",original);
            body.put("user",user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HPair<Integer,String> pair =HttpUtil.postByJsonAndToken(
                USER_URL.concat("/").concat(userId).concat("/").concat("password"),
                token.getToken(),body.toString());

        if(pair.getFirst()==204)
        {
            OpenStackUser user1 = userMap.get(token.getUsername());
            if(user1 == null) {
                user1 = showUser(token,userId);
            }
            user1.setPassword(password);
            userMap.put(user1.getUsername(),user1);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(OpenStackToken token, String userId) {
        if(HttpUtil.deleteByJsonAndToken(USER_URL.concat("/").concat(userId),token.getToken()).getFirst() == 204)
        {
            userMap.remove(token.getUsername());
            return true;
        }
        return false;
    }

    @Override
    public String createVHost(OpenStackToken token, String hostname, String imageRef, String flavorRef, String userData, String metadata)
    {
        String hostId = null;
        JSONObject body = new JSONObject();
        JSONObject server = new JSONObject();
        JSONArray security_groups = new JSONArray();
        try {
            server.put("name",hostname);
            server.put("imageRef",imageRef);
            server.put("flavorRef",flavorRef);
            server.put("availability_zone","nova");
            server.put("OS-DCF:diskConfig","AUTO");
            if(metadata!=null) {
                server.put("metadata",metadata);
            }
            server.put("security_groups",security_groups.add(new JSONObject().put("name","default")));
            if(userData != null) {
                server.put("user_data", Base64Util.encodeByBase64(userData));
            }
            body.put("server",server);
            HPair<Integer,String> pair = HttpUtil.postByJsonAndToken(
                    COMPUTE_URL.concat("/servers"),token.getToken(),body.toString());
            if(pair.getFirst() == 202) {
                server = JSONObject.parseObject(pair.getSecond()).getJSONObject("server");
                hostId = server.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hostId;
    }
    @Override
    public boolean deleteVHost(OpenStackToken token, String hostId)
    {
        String url = COMPUTE_URL.concat("/servers/").concat(hostId);
        HPair<Integer,String> pair = HttpUtil.deleteByJsonAndToken(url,token.getToken());
        return pair.getFirst() == 204;
    }

    public VirtualHost getHostInfo(OpenStackToken token, String hostId)
    {
        VirtualHost host = null;
        try {
            OpenStackUser user = userMap.get(token.getUsername());
            if(user == null) {
                return null;
            }
            String team = user.getTeam();
            String userId = user.getId();
            String tokenId = token.getToken();
            host = new VirtualHost();
            host.setTeam(team);
            host.setUser(userId);
            String url = COMPUTE_URL.concat("/").concat(team).concat("/servers/").concat(hostId);
            HPair<Integer,String> pair = HttpUtil.getByJsonAndToken(url,tokenId);

            if(pair.getFirst() == 200) {
                JSONObject server = JSONObject.parseObject(pair.getSecond()).getJSONObject("server");
                OpenStackUtil.parseHostInfo(server,host);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return host;
    }
    @Override
    public List<VirtualHost> getAllHostInfo(OpenStackToken token)
    {
        List<VirtualHost> hosts = null;
        try {
            OpenStackUser user = userMap.get(token.getUsername());
            if(user == null) {
                return null;
            }
            String team = user.getTeam();
            String userId = user.getId();
            String tokenId = token.getToken();
            String url = COMPUTE_URL.concat("/").concat(team).concat("/servers/detail");
            HPair<Integer,String> pair = HttpUtil.getByJsonAndToken(url,tokenId);
            if(pair.getFirst() == 200)
            {
                hosts = new ArrayList<>();
                JSONArray servers = JSONObject.parseObject(pair.getSecond()).getJSONArray("servers");
                for (int i = 0; i < servers.size(); i++) {
                    VirtualHost host = new VirtualHost(team,userId);
                    JSONObject server = servers.getJSONObject(i);
                    OpenStackUtil.parseHostInfo(server,host);
                    hosts.add(host);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hosts;
    }

    private static boolean action(OpenStackToken token, String hostId, String action)
    {
        String url = COMPUTE_URL.concat("/servers/").concat(hostId).concat("/action");
        Map<String,Object> map = new HashMap<>();
        map.put(action,null);
        JSONObject body = new JSONObject(map);
        HPair<Integer,String> pair = HttpUtil.postByJsonAndToken(url,token.getToken(),body.toString());
        return pair.getFirst() == 202;
    }
    private static boolean action(OpenStackToken token, String hostId, String action, JSONObject actionValue)
    {
        String url = COMPUTE_URL.concat("/servers/").concat(hostId).concat("/action");
        JSONObject body = new JSONObject();
        try {
            body.put(action,actionValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HPair<Integer,String> pair = HttpUtil.postByJsonAndToken(url,token.getToken(),body.toString());
        return pair.getFirst() == 202;
    }

    @Override
    public boolean forceDeleteVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"forceDelete");
    }
    @Override
    public boolean startVHost(OpenStackToken token, String hostId) {
        return action(token,hostId,"os-start");
    }
    @Override
    public boolean stopVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"os-stop");
    }
    @Override
    public boolean rebootVHost(OpenStackToken token, String hostId, boolean graceful)
    {
        try {
            JSONObject type = new JSONObject();
            if(graceful)
                type.put("type","SOFT");
            else
                type.put("type","HARD");
            return action(token,hostId,"reboot",type);
        } catch (JSONException e) {
            return false;
        }
    }
    @Override
    public boolean pauseVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"pause");
    }
    @Override
    public boolean unpauseVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"unpause");
    }
    @Override
    public boolean lockVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"lock");
    }
    @Override
    public boolean unlockVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"unlock");
    }
    @Override
    public boolean suspendVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"suspend");
    }
    @Override
    public boolean resumeVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"resume");
    }

    //搁置
    @Override
    public boolean shelveVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"shelve");
    }
    @Override
    public boolean unshelveVHost(OpenStackToken token, String hostId)
    {
        return action(token,hostId,"unshelve");
    }

    @Override
    public String showConsoleOutput(OpenStackToken token, String hostId, int line)
    {
        String result = null;
        String url = COMPUTE_URL.concat("/servers/").concat(hostId).concat("/action");
        try {
            JSONObject body = new JSONObject().fluentPut("os-getConsoleOutput",new JSONObject().fluentPut("length",line));
            HPair<Integer,String> pair = HttpUtil.postByJsonAndToken(url,token.getToken(),body.toString());
            result =  pair.getFirst()==200? pair.getSecond():null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }



    @Override
    public String getRDPConsole(OpenStackToken token, String hostId) {
        String url = COMPUTE_URL.concat("/servers/").concat(hostId).concat("/action");
        JSONObject body = new JSONObject();
        JSONObject console = new JSONObject();
        try {
            console.put("type","rdp-html5");
            body.put("os-getRDPConsole",console);
            HPair<Integer,String> pair = HttpUtil.postByJsonAndToken(url,token.getToken(),body.toString());
            if(pair.getFirst() == 200)
            {
                JSONObject object = JSONObject.parseObject(pair.getSecond());
                return object.getJSONObject("console").getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String getSerialConsole(OpenStackToken token, String hostId) {
        String url = COMPUTE_URL.concat("/servers/").concat(hostId).concat("/action");
        JSONObject body = new JSONObject();
        JSONObject console = new JSONObject();
        try {
            console.put("type","serial");
            body.put("os-getSerialConsole",console);
            HPair<Integer,String> pair = HttpUtil.postByJsonAndToken(url,token.getToken(),body.toString());
            if(pair.getFirst() == 200)
            {
                JSONObject object = JSONObject.parseObject(pair.getSecond());
                return object.getJSONObject("console").getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String getVNCConsole(OpenStackToken token, String hostId) {
        String url = COMPUTE_URL.concat("/servers/").concat(hostId).concat("/action");
        JSONObject body = new JSONObject();
        JSONObject console = new JSONObject();
        try {
            console.put("type","novnc");
            body.put("os-getVNCConsole",console);
            HPair<Integer,String> pair = HttpUtil.postByJsonAndToken(url,token.getToken(),body.toString());
            if(pair.getFirst() == 200)
            {
                JSONObject object = JSONObject.parseObject(pair.getSecond());
                return object.getJSONObject("console").getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public VirtualHostFlavor showFlavor(OpenStackToken token, String flavorId) {
        VirtualHostFlavor flavor = null;
        String url = COMPUTE_URL.concat("/flavors/").concat(flavorId);
        HPair<Integer,String> pair = HttpUtil.getByJsonAndToken(url,token.getToken());
        if(pair.getFirst() == 200) {
            flavor = OpenStackUtil.parseFlavor(pair.getSecond());
        }
        return flavor;
    }

    @Override
    public List<VirtualHostFlavor> listFlavor(OpenStackToken token) {
        List<VirtualHostFlavor> list = null;
        String url = COMPUTE_URL.concat("/").concat("/flavors");
        HPair<Integer,String> pair = HttpUtil.getByJsonAndToken(url,token.getToken());
        if(pair.getFirst() == 200) {
            list = OpenStackUtil.parseFlavors(pair.getSecond());
        }
        return list;
    }

    @Override
    public List<VirtualHostFlavor> listFlavorDetail(OpenStackToken token) {
        List<VirtualHostFlavor> list = null;
        String url = COMPUTE_URL.concat("/").concat("/flavors/detail");
        HPair<Integer,String> pair = HttpUtil.getByJsonAndToken(url,token.getToken());
        if(pair.getFirst() == 200)
        {
            list = OpenStackUtil.parseFlavors(pair.getSecond());
        }
        return list;
    }

    @Override
    public VirtualHostImage showImage(OpenStackToken token, String imageId) {
        VirtualHostImage image = null;
        String url = IMAGE_URL.concat("/").concat(imageId);
        HPair<Integer,String> pair = HttpUtil.getByJsonAndToken(url,token.getToken());
        if(pair.getFirst() == 200) {
            image =  OpenStackUtil.parseImage(pair.getSecond());
        }
        return image;
    }

    @Override
    public List<VirtualHostImage> listImage(OpenStackToken token) {
        List<VirtualHostImage> list = new ArrayList<>();
        HPair<Integer,String> pair = HttpUtil.getByJsonAndToken(IMAGE_URL,token.getToken());
        if(pair.getFirst() == 200) {
            list = OpenStackUtil.parseImages(pair.getSecond());
        }
        return list;
    }

}

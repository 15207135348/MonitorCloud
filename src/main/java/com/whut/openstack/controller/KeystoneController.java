package com.whut.openstack.controller;

import com.alibaba.fastjson.JSONArray;
import com.whut.openstack.domain.OpenStackTeam;
import com.whut.openstack.domain.OpenStackToken;
import com.whut.openstack.domain.OpenStackUser;
import com.whut.openstack.service.OpenStackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 *
 * 管理员admin 密码root
 * @author yy
 * @time 2018-03-26
 */
@Controller
@RequestMapping("/keystone")
public class KeystoneController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OpenStackService openStackService;

    @Autowired
    public KeystoneController(OpenStackService openStackService) {
        this.openStackService = openStackService;
    }

    @PostMapping(value = "/createTeam")
    @ResponseBody
    public String createTeam(HttpServletRequest request)
    {
        String teamName = request.getParameter("teamName");
        String description = request.getParameter("description");
        OpenStackToken token = openStackService.adminToken();
        OpenStackTeam tenant = openStackService.createTenant(token,teamName,description);
        return tenant==null?"404":tenant.toString();
    }

    @PostMapping(value = "/deleteTeam")
    @ResponseBody
    public String deleteTeam(HttpServletRequest request)
    {
        String teamId = request.getParameter("teamId");
        OpenStackToken token = openStackService.adminToken();
        return openStackService.deleteTenant(token,teamId) ? "200" : "404";
    }


    @PostMapping(value = "/listTeam")
    @ResponseBody
    public String listTeam(HttpServletRequest request)
    {
        OpenStackToken token = openStackService.adminToken();
        List<OpenStackTeam> list = openStackService.listTenant(token);
        if(list == null)
        {
            return "404";
        }
        JSONArray array = new JSONArray();
        for(OpenStackTeam tenant : list)
        {
            array.add(tenant.toString());
        }
        return array.toString();
    }

    @PostMapping(value = "/showTeam")
    @ResponseBody
    public String showTeam(HttpServletRequest request)
    {
        String teamId = request.getParameter("teamId");
        OpenStackToken token = openStackService.adminToken();
        OpenStackTeam tenant = openStackService.showTenant(token,teamId);
        return tenant==null?"404":tenant.toString();
    }

    @PostMapping(value = "/listTeamOfUser")
    @ResponseBody
    public String listTeamOfUser(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user == null){
            return "401";
        }
        OpenStackToken token = openStackService.getToken(user.getUsername());
        List<OpenStackTeam> list = openStackService.listTenantOfUser(token,user.getId());
        if(list == null)
        {
            return "404";
        }
        JSONArray array = new JSONArray();
        for(OpenStackTeam tenant : list)
        {
            array.add(tenant.toString());
        }
        return array.toString();
    }

    @PostMapping(value = "/createUser")
    @ResponseBody
    public String createUser(HttpServletRequest request)
    {
        String domain = request.getParameter("domain");
        String team = request.getParameter("team");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String description = request.getParameter("description");
        OpenStackUser user = new OpenStackUser(username,password,domain,team,description);
        OpenStackToken token = openStackService.adminToken();
        user = openStackService.createUser(token,user);
        return user.getId()==null?"404":user.toString();
    }

    @PostMapping(value = "/loginUser")
    @ResponseBody
    public String loginUser(HttpServletRequest request)
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        OpenStackUser openStackUser = openStackService.getUser(username);
        if(openStackUser==null) {
            return "404";//用户不存在！
        }else if(!openStackUser.getPassword().equals(password)) {
            return "401";//密码错误！
        }else{
            request.getSession().setAttribute("openStackUser",openStackUser);
            return "200";
        }
    }

    @PostMapping(value = "/deleteUser")
    @ResponseBody
    public String deleteUser(HttpServletRequest request)
    {
        String userId = request.getParameter("userId");
        OpenStackToken token = openStackService.adminToken();
        return openStackService.deleteUser(token,userId)?"404":"200";
    }

    @PostMapping(value = "/listUser")
    @ResponseBody
    public String listUser(HttpServletRequest request)
    {
        OpenStackToken token = openStackService.adminToken();
        List<OpenStackUser> list = openStackService.listUser(token);
        if(list == null)
        {
            return "404";
        }
        JSONArray array = new JSONArray();
        for(OpenStackUser user : list)
        {
            array.add(user.toString());
        }
        return array.toString();
    }
    @PostMapping(value = "/showUser")
    @ResponseBody
    public String showUser(HttpServletRequest request)
    {
        String userId = request.getParameter("userId");
        OpenStackToken token = openStackService.adminToken();
        OpenStackUser user = openStackService.showUser(token,userId);
        return user == null ? "404" : user.toString();
    }
    @PostMapping(value = "/changePassword")
    @ResponseBody
    public String changePassword(HttpServletRequest request)
    {
        String userId = request.getParameter("userId");
        String original = request.getParameter("original");
        String password = request.getParameter("password");
        OpenStackToken token = openStackService.adminToken();
        return openStackService.changePassword(token,userId,original,password) ? "200" : "404";
    }
}

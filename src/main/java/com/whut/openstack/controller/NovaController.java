package com.whut.openstack.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whut.openstack.domain.*;
import com.whut.common.entity.User;
import com.whut.openstack.service.OpenStackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/nova")
public class NovaController {

    private final OpenStackService openStackService;

    @Autowired
    public NovaController(OpenStackService openStackService) {
        this.openStackService = openStackService;
    }

    @PostMapping(value = "/action")
    @ResponseBody //表示返回的是数据
    public String action(HttpServletRequest request) {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            String username = user.getUsername();
            OpenStackToken token = openStackService.getToken(username);
            String hostId = request.getParameter("hostId");
            String action = request.getParameter("action");
            switch (action){
                case "start":return openStackService.startVHost(token,hostId)+"";
                case "stop":return openStackService.stopVHost(token,hostId)+"";
                case "suspend":return openStackService.suspendVHost(token,hostId)+"";
                case "resume":return openStackService.resumeVHost(token,hostId)+"";
                case "pause":return openStackService.pauseVHost(token,hostId)+"";
                case "unpause":return openStackService.unpauseVHost(token,hostId)+"";
                case "reboot":
                    Boolean graceful = Boolean.valueOf(request.getParameter("graceful"));
                    return openStackService.rebootVHost(token,hostId,graceful)+"";
                case "lock":return openStackService.lockVHost(token,hostId)+"";
                case "unlock":return openStackService.unlockVHost(token,hostId)+"";
                case "forceDelete":return openStackService.forceDeleteVHost(token,hostId)+"";
                case "delete":return openStackService.deleteVHost(token,hostId)+"";
                case "shelve":return openStackService.shelveVHost(token,hostId)+"";
                case "unshelve":return openStackService.unshelveVHost(token,hostId)+"";
            }
        }
        return "401";
    }
    @GetMapping(value = "/connectHost")
    public void connectHost(HttpServletRequest request, HttpServletResponse response)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            String username = user.getUsername();
            OpenStackToken token = openStackService.getToken(username);
            String hostId = request.getParameter("hostId");
            try {
                response.sendRedirect(openStackService.getVNCConsole(token,hostId));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @PostMapping(value = "/createVHost")
    @ResponseBody //表示返回的是数据
    public String createVHost(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            String username = user.getUsername();
            OpenStackToken token = openStackService.getToken(username);
            String hostname = request.getParameter("hostname");
            String image = request.getParameter("image");
            String flavor = request.getParameter("flavor");
            String userData = request.getParameter("userData");
            String metadata = request.getParameter("metadata");
            return openStackService.createVHost(token,hostname,image,flavor,userData,metadata);
        }
        return "401";
    }
    @PostMapping(value = "/getVHostInfo")
    @ResponseBody //表示返回的是数据
    public String getVHostInfo(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            String username = user.getUsername();
            OpenStackToken token = openStackService.getToken(username);
            String hostId = request.getParameter("hostId");
            return openStackService.getHostInfo(token,hostId).toString();
        }
        return "401";
    }
    @PostMapping(value = "/getAllVHostInfo")
    @ResponseBody //表示返回的是数据
    public String getAllVHostInfo(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            JSONObject jsonObject = new JSONObject();
            String username = user.getUsername();
            OpenStackToken token = openStackService.getToken(username);
            List<VirtualHost> virtualHosts = openStackService.getAllHostInfo(token);
            JSONArray array = new JSONArray();
            for(VirtualHost host : virtualHosts) {
                array.add(host.toString());
            }
            jsonObject.put("code",0);
            jsonObject.put("msg","");
            jsonObject.put("count",array.size());
            jsonObject.put("data",array);
            return jsonObject.toString();
        }
        return "401";
    }
    @PostMapping(value = "/createRemoteConsole")
    @ResponseBody
    public String createRemoteConsole(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            String hostId = request.getParameter("hostId");
            OpenStackToken token = openStackService.getToken(user.getUsername());
            String url = openStackService.getVNCConsole(token,hostId);
            return url==null ? "404" : url;
        }
        return "401";
    }

    @PostMapping(value = "/showFlavor")
    @ResponseBody
    public String showFlavor(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            String flavorId = request.getParameter("flavorId");
            OpenStackToken token = openStackService.getToken(user.getUsername());
            VirtualHostFlavor flavor = openStackService.showFlavor(token,flavorId);
            return flavor==null?"404":flavor.toString();
        }
        return "401";
    }
    @PostMapping(value = "/showFlavors")
    @ResponseBody
    public String showFlavors(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            OpenStackToken token = openStackService.getToken(user.getUsername());
            List<VirtualHostFlavor> list = openStackService.listFlavor(token);
            if (list == null) {
                return "404";
            }
            JSONArray array = new JSONArray();
            for (VirtualHostFlavor flavor : list) {
                array.add(flavor.toString());
            }
            return array.toString();
        }
        return "401";
    }
    @PostMapping(value = "/showFlavorsDetail")
    @ResponseBody
    public String showFlavorsDetail(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            OpenStackToken token = openStackService.getToken(user.getUsername());
            List<VirtualHostFlavor> list = openStackService.listFlavorDetail(token);
            if (list == null) {
                return "404";
            }
            JSONArray array = new JSONArray();
            for (VirtualHostFlavor flavor : list) {
                array.add(flavor.toString());
            }
            return array.toString();
        }
        return "401";
    }

    @PostMapping(value = "/showImage")
    @ResponseBody
    public String showImage(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            OpenStackToken token = openStackService.getToken(user.getUsername());
            String imageId = request.getParameter("imageId");
            return openStackService.showImage(token,imageId).toString();
        }
        return "401";
    }
    @PostMapping(value = "/showImages")
    @ResponseBody
    public String showImages(HttpServletRequest request)
    {
        OpenStackUser user = (OpenStackUser) request.getSession().getAttribute("openStackUser");
        if (user != null) {
            OpenStackToken token = openStackService.getToken(user.getUsername());
            List<VirtualHostImage> list= openStackService.listImage(token);
            if (list == null) {
                return "404";
            }
            JSONArray array = new JSONArray();
            for (VirtualHostImage image : list) {
                array.add(image.toString());
            }
            return array.toString();
        }
        return "401";
    }
}

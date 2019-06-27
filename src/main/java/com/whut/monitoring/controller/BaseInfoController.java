package com.whut.monitoring.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whut.common.util.TimeFormatUtil;
import com.whut.monitoring.entity.MonitoredHost;
import com.whut.common.entity.User;
import com.whut.monitoring.service.MonitoredHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/baseInfo")
public class BaseInfoController {

    private final
    MonitoredHostService monitoredHostService;

    @Autowired
    public BaseInfoController(MonitoredHostService monitoredHostService) {
        this.monitoredHostService = monitoredHostService;
    }

    /**
     * @since 18-8-1 下午4:52
     * @author 杨赟
     * @describe
     * @param request 请求
     * @return 列出被监控主机
     */
    @PostMapping(value = "/listMonitoredHosts")
    @ResponseBody
    public JSONObject listMonitoredHosts(HttpServletRequest request)
    {
        JSONObject object = new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            object.put("success",false);
            object.put("message","用户登录超时");
            return object;
        }
        //被监控主机列表
        JSONArray hosts = new JSONArray();
        //通过用户名获取物理主机
        List<MonitoredHost> monitoredHosts = monitoredHostService.findHostByUsername(user.getUsername());
        if (monitoredHosts != null) {
            for (MonitoredHost monitoredHost : monitoredHosts) {
                JSONObject host = new JSONObject();
                host.put("name", monitoredHost.getHostname());
                host.put("system",monitoredHost.getSystem());
                host.put("mac", monitoredHost.getMac());
                host.put("ipv4", monitoredHost.getIpv4());
                host.put("cpu", monitoredHost.getCpu());
                host.put("ram", monitoredHost.getRam());
                host.put("disk", monitoredHost.getDisk());
                host.put("connected",monitoredHost.isConnected());
                host.put("duration",TimeFormatUtil.getDuration(monitoredHost.getTimestamp(),System.currentTimeMillis()));
                hosts.add(host);
            }
        }
        object.put("success",true);
        object.put("message",hosts);
        return object;
    }

    @PostMapping(value = "/listImportedHosts")
    @ResponseBody
    public JSONObject listImportedHosts(HttpServletRequest request){
        JSONObject object = new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            object.put("success",false);
            object.put("message","用户登录超时");
            return object;
        }
        //被监控主机列表
        JSONArray hosts = new JSONArray();
        //通过用户名获取物理主机
        List<MonitoredHost> monitoredHosts = monitoredHostService.findHostByUsername(user.getUsername());
        if (monitoredHosts != null) {
            for (MonitoredHost monitoredHost : monitoredHosts) {
                JSONObject host = new JSONObject();
                host.put("name", monitoredHost.getHostname());
                host.put("system",monitoredHost.getSystem());
                host.put("mac", monitoredHost.getMac());
                host.put("ipv4", monitoredHost.getIpv4());
                host.put("cpu", monitoredHost.getCpu());
                host.put("ram", monitoredHost.getRam());
                host.put("disk", monitoredHost.getDisk());
                host.put("connected",monitoredHost.isConnected() ? "已连接":"已断线");
                host.put("time",TimeFormatUtil.stampToTime(monitoredHost.getTimestamp(),"yyyy-MM-dd"));
                hosts.add(host);
            }
        }
        object.put("code",0);
        object.put("msg","");
        object.put("count",hosts.size());
        object.put("data",hosts);
        return object;
    }
}

package com.whut.monitoring.controller;

import com.alibaba.fastjson.JSONObject;
import com.whut.monitoring.config.MonitorAgentConfig;
import com.whut.common.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yy
 * @describe 生成安装脚本供用户一键安装数据采集客户端
 * @time 18-8-5 下午6:29
 */
@Controller
@RequestMapping(value = "/monitorAgent")
public class MonitorAgentController {

    @PostMapping(value = "/getInstallScript")
    @ResponseBody
    public JSONObject getInstallScript(HttpServletRequest request)
    {
        JSONObject object = new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            object.put("success",false);
            object.put("message","用户登录超时");
            return object;
        }
        int internal = Integer.parseInt(request.getParameter("internal"));
        boolean selfStart = Boolean.parseBoolean(request.getParameter("selfStart"));
        String script = MonitorAgentConfig.getScript(internal,selfStart,user.getUsername());
        object.put("success",true);
        object.put("message",script);
        return object;
    }
}

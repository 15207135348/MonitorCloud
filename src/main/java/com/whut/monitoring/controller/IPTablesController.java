package com.whut.monitoring.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whut.monitoring.entity.IPTables;
import com.whut.monitoring.service.IPTablesService;
import com.whut.monitoring.util.IptablesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 杨赟
 * @describe
 * @since 19-1-19 下午9:22
 */
@Controller
@RequestMapping("/iptables")
public class IPTablesController {

    private final
    IPTablesService ipTablesService;

    @Autowired
    public IPTablesController(IPTablesService ipTablesService) {
        this.ipTablesService = ipTablesService;
    }

    /**
     * @since 19-1-19 下午9:23
     * @author 杨赟
     * @describe
     */
    @PostMapping(value = "/listIP")
    @ResponseBody
    public Object listIP(HttpServletRequest request){

        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        List<IPTables> ipTablesList = ipTablesService.findAll();
        for (IPTables ipTables : ipTablesList)
        {
            array.add(ipTables.toJSON());
        }
        object.put("code",0);
        object.put("msg","");
        object.put("count",array.size());
        object.put("data",array);
        return object;
    }

    /**
     * @since 19-1-19 下午9:23
     * @author 杨赟
     * @describe
     */
    @PostMapping(value = "/addIP")
    @ResponseBody
    public Object addIP(HttpServletRequest request){

        JSONObject result = new JSONObject();
        String ip = request.getParameter("ip");
        String reason = request.getParameter("reason");
        if (IptablesUtil.disableIP(ip)) {
            ipTablesService.updateByIp(ip, new IPTables(ip,reason));
            result.put("success",true);
            result.put("message","成功禁用IP");
        }else {
            result.put("success",false);
            result.put("message","IP格式错误");
        }
        return result;
    }

    /**
     * @since 19-1-19 下午9:23
     * @author 杨赟
     * @describe
     */
    @PostMapping(value = "/delIP")
    @ResponseBody
    public Object delIP(HttpServletRequest request){

        JSONObject result = new JSONObject();
        String[] ips = request.getParameter("ip").split(",");
        for (String ip : ips)
        {
            if (IptablesUtil.enableIP(ip)){
                ipTablesService.deleteByIp(ip);
                result.put("success",true);
                result.put("message","成功删除IP");
            } else {
                result.put("success",false);
                result.put("message","IP格式错误");
            }
        }
        return result;
    }

    /**
     * @since 19-1-19 下午9:23
     * @author 杨赟
     * @describe
     */
    @PostMapping(value = "/edit")
    @ResponseBody
    public Object editIP(HttpServletRequest request){

        JSONObject result = new JSONObject();
        int id = Integer.parseInt(request.getParameter("id"));
        String field = request.getParameter("field");
        String value = request.getParameter("value");
        if (field.equals("ip"))
        {
            IPTables ipTables = ipTablesService.findById(id);
            if (IptablesUtil.enableIP(ipTables.getIp())
                    && IptablesUtil.disableIP(value)) {
                ipTablesService.updateById(id,new IPTables(value,ipTables.getReason()));
                result.put("success",true);
                result.put("message","编辑成功");
            } else {
                result.put("success",false);
                result.put("message","IP格式错误");
            }
            return result;
        }
        else if (field.equals("reason"))
        {
            IPTables ipTables = ipTablesService.findById(id);
            ipTablesService.updateById(id, new IPTables(ipTables.getIp(),value));
            result.put("success",true);
            result.put("message","编辑成功");
            return result;
        }
        result.put("success",false);
        result.put("message","未知的field");
        return result;
    }
}

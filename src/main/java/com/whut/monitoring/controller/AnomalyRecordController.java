package com.whut.monitoring.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.whut.common.dao.mongodb.IMongoDBDao;
import com.whut.monitoring.entity.AnomalyRecord;
import com.whut.monitoring.entity.MonitoredHost;
import com.whut.monitoring.service.AnomalyRecordService;
import com.whut.monitoring.service.MonitoredHostService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/anomalyRecord")
public class AnomalyRecordController {

    private final
    AnomalyRecordService anomalyRecordService;


    private final
    MonitoredHostService monitoredHostService;

    @Autowired
    public AnomalyRecordController(AnomalyRecordService anomalyRecordService,
                                   MonitoredHostService monitoredHostService) {
        this.anomalyRecordService = anomalyRecordService;
        this.monitoredHostService = monitoredHostService;
    }

    @PostMapping(value = "/listRecord")
    @ResponseBody
    public Object listRecord(HttpServletRequest request){
        List<AnomalyRecord> list = anomalyRecordService.findAll();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (AnomalyRecord record : list)
        {
            String host = monitoredHostService.findHostByMac(record.getMac()).getHostname();
            array.add(record.toJSON().fluentPut("host",host));
        }
        object.put("code",0);
        object.put("msg","");
        object.put("count",array.size());
        object.put("data",array);
        return object;
    }

    @PostMapping(value = "/delById")
    @ResponseBody
    public Object delById(HttpServletRequest request){
        String[] ids = request.getParameter("id").split(",");
        for (String id : ids)
        {
            anomalyRecordService.deleteById(Integer.parseInt(id));
        }
        return new JSONObject().fluentPut("success",true);
    }

    @PostMapping(value = "/delByMac")
    @ResponseBody
    public Object delByMac(HttpServletRequest request){
        String mac = request.getParameter("mac");
        anomalyRecordService.deleteByMac(mac);
        return new JSONObject().fluentPut("success",true);
    }
}

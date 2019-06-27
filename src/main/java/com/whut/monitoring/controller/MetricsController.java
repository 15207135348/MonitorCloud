package com.whut.monitoring.controller;

import com.whut.common.util.TimeFormatUtil;
import com.whut.monitoring.service.IHBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/metrics")
public class MetricsController {

    private final IHBaseService hBaseService;

    @Autowired
    public MetricsController(IHBaseService hBaseService) {
        this.hBaseService = hBaseService;
    }

    /**
     * @since 18-7-30 上午9:12
     * @author 杨赟
     * @describe
     * @param
     * @return
     */
    @PostMapping(value = "/findCpuByTime")
    @ResponseBody
    public String findCpuByTime(HttpServletRequest request){
        String macAddress = request.getParameter("mac");
        long startTime = Long.parseLong(request.getParameter("start"))/1000;
        long stopTime = Long.parseLong(request.getParameter("stop"))/1000;
        System.out.println(TimeFormatUtil.timestamp2Date(startTime,null)+"-"+TimeFormatUtil.timestamp2Date(stopTime,null));
        return hBaseService.findCpuByTime(macAddress,startTime,stopTime).toString();
    }

    @PostMapping(value = "/findRamByTime")
    @ResponseBody
    public String findRamByTime(HttpServletRequest request){
        String macAddress = request.getParameter("mac");
        long startTime = Long.parseLong(request.getParameter("start"))/1000;
        long stopTime = Long.parseLong(request.getParameter("stop"))/1000;
        return hBaseService.findRamByTime(macAddress,startTime,stopTime).toString();
    }
    @PostMapping(value = "/findDiskSpeedByTime")
    @ResponseBody
    public String findDiskSpeedByTime(HttpServletRequest request){
        String macAddress = request.getParameter("mac");
        long startTime = Long.parseLong(request.getParameter("start"))/1000;
        long stopTime = Long.parseLong(request.getParameter("stop"))/1000;
        return hBaseService.findDiskSpeedByTime(macAddress,startTime,stopTime).toString();
    }
    @PostMapping(value = "/findDiskCountByTime")
    @ResponseBody
    public String findDiskCountByTime(HttpServletRequest request){
        String macAddress = request.getParameter("mac");
        long startTime = Long.parseLong(request.getParameter("start"))/1000;
        long stopTime = Long.parseLong(request.getParameter("stop"))/1000;
        return hBaseService.findDiskCountByTime(macAddress,startTime,stopTime).toString();
    }

    @PostMapping(value = "/findNetByTime")
    @ResponseBody
    public String findNetByTime(HttpServletRequest request){
        String macAddress = request.getParameter("mac");
        long startTime = Long.parseLong(request.getParameter("start"))/1000;
        long stopTime = Long.parseLong(request.getParameter("stop"))/1000;
        return hBaseService.findNetByTime(macAddress,startTime,stopTime).toString();
    }
}

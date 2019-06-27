package com.whut.monitoring.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.whut.monitoring.service.DetectionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yy
 * @describe
 * @time 18-11-23 下午6:22
 */
@Service
public class DetectionServiceImpl implements DetectionService{

    private static Map<String,List<Long>> anomalyMap = new HashMap<>();



    @Override
    public boolean isAbnormal(String mac, long time, JSONObject data) {
        List<Long> anomalies = anomalyMap.get(mac);
        if (anomalies == null) {
            anomalies = new ArrayList<>();
        }
        anomalies.add(time);
        return false;
    }

    @Override
    public String getReport(String mac, long time, JSONObject data) {
        return "";
    }
}

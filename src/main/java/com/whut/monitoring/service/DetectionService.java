package com.whut.monitoring.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author yy
 * @describe
 * @time 18-11-23 下午6:11
 */
public interface DetectionService {

    boolean isAbnormal(String mac, long time, JSONObject data);
    String getReport(String mac, long time, JSONObject data);

}

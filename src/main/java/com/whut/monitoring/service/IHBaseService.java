package com.whut.monitoring.service;

import com.alibaba.fastjson.JSONArray;

public interface IHBaseService {

    JSONArray findCpuByTime(String macAddress, long startTime, long stopTime);
    JSONArray findRamByTime(String macAddress, long startTime, long stopTime);
    JSONArray findDiskCountByTime(String macAddress, long startTime, long stopTime);
    JSONArray findDiskSpeedByTime(String macAddress, long startTime, long stopTime);
    JSONArray findNetByTime(String macAddress, long startTime, long stopTime);
}

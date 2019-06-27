package com.whut.monitoring.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.whut.common.config.HBaseConfig;
import com.whut.common.dao.hbase.IHBaseDao;
import com.whut.monitoring.service.IHBaseService;
import com.whut.common.util.HBaseDaoUtil;
import org.apache.hadoop.hbase.client.Scan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HBaseServiceImpl implements IHBaseService {

    private final
    IHBaseDao hBaseDao;

    @Autowired
    public HBaseServiceImpl(IHBaseDao hBaseDao) {
        this.hBaseDao = hBaseDao;
    }

    private JSONArray find(Scan scan, String macAddress, long startTime, long stopTime)
    {
        scan = HBaseDaoUtil.rowStart(scan,macAddress+ startTime);
        scan = HBaseDaoUtil.rowStop(scan,macAddress+stopTime);
        return HBaseDaoUtil.parseScanner(hBaseDao.scan(HBaseConfig.TABLE_NAME,scan));
    }

    @Override
    public JSONArray findCpuByTime(String macAddress, long startTime, long stopTime) {
        Scan scan = new Scan();
        return find(
                HBaseDaoUtil.qualifierRange(scan,"a","g"),
                macAddress,startTime,stopTime
        );
    }

    @Override
    public JSONArray findRamByTime(String macAddress, long startTime, long stopTime) {
        Scan scan = new Scan();
        return find(
                HBaseDaoUtil.qualifierRange(scan,"h","j"),
                macAddress,startTime,stopTime
        );
    }
    @Override
    public JSONArray findDiskSpeedByTime(String macAddress, long startTime, long stopTime) {
        Scan scan = new Scan();
        return find(
                HBaseDaoUtil.qualifierRange(scan,"k","l"),
                macAddress,startTime,stopTime
        );
    }

    @Override
    public JSONArray findDiskCountByTime(String macAddress, long startTime, long stopTime) {
        Scan scan = new Scan();
        return find(
                HBaseDaoUtil.qualifierRange(scan,"m","n"),
                macAddress,startTime,stopTime
        );
    }
    @Override
    public JSONArray findNetByTime(String macAddress, long startTime, long stopTime) {
        Scan scan = new Scan();
        return find(
                HBaseDaoUtil.qualifierRange(scan,"o","p"),
                macAddress,startTime,stopTime
        );
    }

}

package com.whut.monitoring.service;

import com.whut.monitoring.entity.IPTables;
import com.whut.monitoring.entity.MonitoredHost;

import java.util.List;

public interface IPTablesService {

    void addIPTables(IPTables ipTables);
    void deleteByIp(String ip);
    void updateById(int id, IPTables ipTables);
    void updateByIp(String ip, IPTables ipTables);
    IPTables findById(int id);
    IPTables findByIp(String ip);
    List<IPTables> findAll();
}

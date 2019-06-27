package com.whut.monitoring.service;

import com.whut.monitoring.entity.MonitoredHost;

import java.util.List;

public interface MonitoredHostService {

    void addHost(MonitoredHost host);
    void deleteHostByMac(String mac);
    void updateHostByMac(String mac, MonitoredHost host);
    void setConnectedByMac(String mac,boolean connected);
    MonitoredHost findHostByMac(String mac);
    MonitoredHost findByHostname(String hostname);
    List<MonitoredHost> findHostByUsername(String username);
    List<MonitoredHost> findAllHost();
}

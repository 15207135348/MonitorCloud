package com.whut.monitoring.service.impl;

import com.whut.monitoring.dao.MonitoredHostRepository;
import com.whut.monitoring.entity.MonitoredHost;
import com.whut.monitoring.service.MonitoredHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MonitoredHostServiceImpl implements MonitoredHostService {

    private final
    MonitoredHostRepository hostRepository;

    @Autowired
    public MonitoredHostServiceImpl(MonitoredHostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    @Override
    public void addHost(MonitoredHost monitoredHost) {
        hostRepository.save(monitoredHost);
    }

    @Override
    public void deleteHostByMac(String mac) {
        hostRepository.deleteByMac(mac);
    }

    @Override
    public void updateHostByMac(String mac, MonitoredHost host) {
        MonitoredHost host1=hostRepository.findByMac(mac);
        if(host1!=null) {
            host.setId(host1.getId());
        }
        hostRepository.save(host);
    }

    @Override
    public void setConnectedByMac(String mac, boolean connected) {
        MonitoredHost host=hostRepository.findByMac(mac);
        if(host!=null) {
            host.setConnected(connected);
        }
        hostRepository.save(host);
    }

    @Override
    public MonitoredHost findHostByMac(String mac) {
        return hostRepository.findByMac(mac);
    }

    @Override
    public MonitoredHost findByHostname(String hostname) {
        return hostRepository.findByHostname(hostname);
    }

    @Override
    public List<MonitoredHost> findHostByUsername(String username) {
        return hostRepository.findByUsername(username);
    }

    @Override
    public List<MonitoredHost> findAllHost() {
        return hostRepository.findAll();
    }
}

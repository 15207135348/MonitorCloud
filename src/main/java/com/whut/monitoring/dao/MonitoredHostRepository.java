package com.whut.monitoring.dao;

import com.whut.monitoring.entity.MonitoredHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoredHostRepository extends JpaRepository<MonitoredHost,Integer> {

    MonitoredHost findByMac(String mac);
    MonitoredHost findByHostname(String hostname);
    void deleteByMac(String mac);
    List<MonitoredHost> findByUsername(String username);
    List<MonitoredHost> findAll();
}

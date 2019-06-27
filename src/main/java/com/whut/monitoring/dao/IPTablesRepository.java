package com.whut.monitoring.dao;

import com.whut.monitoring.entity.IPTables;
import com.whut.monitoring.entity.MonitoredHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPTablesRepository extends JpaRepository<IPTables,Integer> {

    IPTables findById(int id);
    IPTables findByIp(String ip);
    List<IPTables> findAll();
    void deleteByIp(String ip);
}

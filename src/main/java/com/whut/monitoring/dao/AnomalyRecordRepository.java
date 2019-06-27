package com.whut.monitoring.dao;

import com.whut.monitoring.entity.AnomalyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnomalyRecordRepository extends JpaRepository<AnomalyRecord,Integer> {

    List<AnomalyRecord> findAll();
    List<AnomalyRecord> findByMac(String mac);
    AnomalyRecord findById(int id);

    void deleteAll();
    void deleteByMac(String mac);
    void deleteById(int id);

}

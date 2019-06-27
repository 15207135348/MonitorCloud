package com.whut.monitoring.service;

import com.whut.monitoring.entity.AnomalyRecord;

import java.util.List;

public interface AnomalyRecordService {

    int addRecord(AnomalyRecord record);
    void updateRecord(int id, AnomalyRecord record);
    void deleteById(int id);
    void deleteByMac(String mac);
    void deleteAll();
    AnomalyRecord findById(int id);
    List<AnomalyRecord> findByMac(String mac);
    List<AnomalyRecord> findAll();
}

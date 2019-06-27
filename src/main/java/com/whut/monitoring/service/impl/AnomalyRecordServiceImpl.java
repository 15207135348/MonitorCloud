package com.whut.monitoring.service.impl;

import com.whut.monitoring.dao.AnomalyRecordRepository;
import com.whut.monitoring.dao.IPTablesRepository;
import com.whut.monitoring.entity.AnomalyRecord;
import com.whut.monitoring.entity.IPTables;
import com.whut.monitoring.service.AnomalyRecordService;
import com.whut.monitoring.service.IPTablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnomalyRecordServiceImpl implements AnomalyRecordService {

    private final
    AnomalyRecordRepository anomalyRecordRepository;

    @Autowired
    public AnomalyRecordServiceImpl(AnomalyRecordRepository anomalyRecordRepository) {
        this.anomalyRecordRepository = anomalyRecordRepository;
    }

    @Override
    public int addRecord(AnomalyRecord record) {
        anomalyRecordRepository.save(record);
        return record.getId();
    }

    @Override
    public void updateRecord(int id, AnomalyRecord record) {
        AnomalyRecord record1 = anomalyRecordRepository.findById(id);
        if (record1!=null) {
            record.setId(record1.getId());
        }
        anomalyRecordRepository.save(record);
    }

    @Override
    public void deleteById(int id) {
        anomalyRecordRepository.deleteById(id);
    }

    @Override
    public void deleteByMac(String mac) {
        anomalyRecordRepository.deleteByMac(mac);
    }

    @Override
    public void deleteAll() {
        anomalyRecordRepository.deleteAll();
    }

    @Override
    public AnomalyRecord findById(int id) {
        return anomalyRecordRepository.findById(id);
    }

    @Override
    public List<AnomalyRecord> findByMac(String mac) {
        return anomalyRecordRepository.findByMac(mac);
    }

    @Override
    public List<AnomalyRecord> findAll() {
        return anomalyRecordRepository.findAll();
    }
}

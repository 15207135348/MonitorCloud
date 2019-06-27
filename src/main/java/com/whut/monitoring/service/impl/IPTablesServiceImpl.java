package com.whut.monitoring.service.impl;

import com.whut.monitoring.dao.IPTablesRepository;
import com.whut.monitoring.entity.IPTables;
import com.whut.monitoring.service.IPTablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IPTablesServiceImpl implements IPTablesService {

    private final
    IPTablesRepository ipTablesRepository;

    @Autowired
    public IPTablesServiceImpl(IPTablesRepository ipTablesRepository) {
        this.ipTablesRepository = ipTablesRepository;
    }

    @Override
    public void addIPTables(IPTables ipTables) {
        ipTablesRepository.save(ipTables);
    }

    @Override
    public void deleteByIp(String ip) {
        ipTablesRepository.deleteByIp(ip);
    }

    @Override
    public void updateById(int id, IPTables ipTables) {
        IPTables ipTables1=ipTablesRepository.findById(id);
        if(ipTables1!=null) {
            ipTables.setId(id);
        }
        ipTablesRepository.save(ipTables);
    }

    @Override
    public void updateByIp(String ip, IPTables ipTables) {
        IPTables ipTables1=ipTablesRepository.findByIp(ip);
        if(ipTables1!=null) {
            ipTables.setId(ipTables1.getId());
        }
        ipTablesRepository.save(ipTables);
    }

    @Override
    public IPTables findById(int id) {
        return ipTablesRepository.findById(id);
    }

    @Override
    public IPTables findByIp(String ip) {
        return ipTablesRepository.findByIp(ip);
    }


    @Override
    public List<IPTables> findAll() {
        return ipTablesRepository.findAll();
    }
}

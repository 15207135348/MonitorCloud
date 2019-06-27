package com.whut.common.dao.hbase;

import org.springframework.stereotype.Component;

/**
 * Created by 杨赟 on 2018-07-12.
 */
public interface IHBaseAdminDao {

    void createTable(String tableName, String... familyNames);
    void dropTable(String tableName);
    void modifyFamilyVersion(String tableName, String family, int maxVersions);
    void addFamily(String tableName, String family, int maxVersions);
}

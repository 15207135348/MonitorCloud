package com.whut.common.dao.hbase;

import org.apache.hadoop.hbase.client.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 杨赟 on 2018-07-12.
 */
public interface IHBaseDao {

    void add(String tableName, Put put);
    void adds(String tableName, List<Put> put);

    void delete(String table, Delete delete);
    void deletes(String table, List<Delete> delete);

    Result find(String table, Get get);
    Result[] finds(String table, List<Get> gets);
    ResultScanner scan(String table, Scan scan);

}

package com.whut.common.dao.hbase.impl;

import com.whut.common.config.HBaseConfig;
import com.whut.common.dao.hbase.IHBaseDao;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by 杨赟 on 2018-07-12.
 */
@Component
public class HBaseDaoImpl implements IHBaseDao {

    //------------------------------单列的操作----------------------------------

    @Override
    public void add(String table, Put put){
        try {
            HBaseConfig.getConnection().getTable(TableName.valueOf(table)).put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void adds(String table, List<Put> puts){
        try {
            HBaseConfig.getConnection().getTable(TableName.valueOf(table)).put(puts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Result find(String table, Get get){
        try {
            return  HBaseConfig.getConnection().getTable(TableName.valueOf(table)).get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Result[] finds(String table, List<Get> gets) {
        try {
            return  HBaseConfig.getConnection().getTable(TableName.valueOf(table)).get(gets);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void delete(String table, Delete delete){
        try {
            HBaseConfig.getConnection().getTable(TableName.valueOf(table)).delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deletes(String table, List<Delete> delete){
        try {
            HBaseConfig.getConnection().getTable(TableName.valueOf(table)).delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultScanner scan(String table, Scan scan) {

        try {
            return  HBaseConfig.getConnection().getTable(TableName.valueOf(table)).
                    getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

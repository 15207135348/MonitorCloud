package com.whut.common.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;


/**
 * Created by 杨赟 on 2018-07-16.
 */
public class HBaseConfig {

    //监控指标表
    public static final String TABLE_NAME = "mc";
    public static final String FAMILY = "f";
    //行键：6C:71:D9:2E:36:13+1531741478 : mac+时间(17+10)
    public static final String ROW_KEY = "%s%10d";
    private static Connection connection = null;
    private static Admin admin = null;
    static {
        try {
            //获得Configuration实例并进行相关设置
            Configuration conf = HBaseConfiguration.create();
//            conf.set("hbase.rootdir","hdfs://localhost:9000/hbase");
            //获得Connection实例
            connection = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    public static Admin getAdmin() {
        return admin;
    }

    public static void close(){
        if(admin!=null)
        {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(connection != null)
        {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

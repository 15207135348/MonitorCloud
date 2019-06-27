package com.whut.common.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by YY on 2018-03-25.
 */
public class MongoDBConfig {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    public static final String DB_NAME = "mc";

    private static MongoDatabase connection = null;
    static {
        // 连接到MongoDB
        MongoClient mongo = new MongoClient(HOST, PORT);
        // 打开数据库
        connection = mongo.getDatabase(DB_NAME);
    }
    public static MongoDatabase getConnection() {
        return connection;
    }
    public static void closeConnection(){
        connection.drop();
    }
}
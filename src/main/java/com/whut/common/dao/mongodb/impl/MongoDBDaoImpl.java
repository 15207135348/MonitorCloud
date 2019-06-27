package com.whut.common.dao.mongodb.impl;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.whut.common.config.MongoDBConfig;
import com.whut.common.dao.mongodb.IMongoDBDao;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MongoDBDaoImpl implements IMongoDBDao{
    
    /**
     * @Title: findDocument
     * @Description:  查询 key 为 value 的 第一份文档
     * @return: Document
     */
    @Override
    public Document findDocument(String collection, String key,Object value){
        return  MongoDBConfig.getConnection().//数据库连接
                getCollection(collection).//集合
                find(new Document(key,value)).//文档迭代器
                first();//第一份
    }

    @Override
    public Document findLastDocument(String collection) {
        long count = MongoDBConfig.getConnection().getCollection(collection).count();
        return findDocumentByPage(collection,
                (int)(count>0?count-1:count),
                1).first();
    }

    @Override
    public FindIterable<Document> findLastMultiDocument(String collection, int count) {
        long total = MongoDBConfig.getConnection().getCollection(collection).count();
        int skip = (int) (total - count);
        return MongoDBConfig.getConnection().//数据库连接
                getCollection(collection).//集合
                find().skip(skip);//文档迭代器
    }

    /**
     * @Title: findDocumentByPage
     * @Description:  分页查询,跳过 skip 页,查询 limit 页
     * @return:
     */
    @Override
    public FindIterable<Document> findDocumentByPage(String collection,int skip, int limit){
        return  MongoDBConfig.getConnection().//数据库连接
                getCollection(collection).//集合
                find().//文档迭代器
                skip(skip).//跳过
                limit(limit);//查询上限
    }

    /**
     * @Title: findDocumentByRange
     * @Description:  范围查询，查询键值在 [start,end)之间的文档
     * @return: void
     */
    @Override
    public FindIterable<Document> findDocumentByRange(String collection,String key, Object start, Object end){
        Document range = new Document();
        range.put("$gte",start);
        range.put("$lt",end);
        return MongoDBConfig.getConnection().
                getCollection(collection).
                find(new Document(key,range));
    }

    @Override
    public FindIterable<Document> findDocumentByMinId(String collection,String id, int limit) {
        return findDocumentByMinId(collection,id).limit(limit);
    }

    @Override
    public FindIterable<Document> findDocumentByTimestamp(String collection, long timestamp) {
        return MongoDBConfig.getConnection().getCollection(collection).
                find(new Document().
                        append("start",new Document("$lte",String.valueOf(timestamp))).
                        append("stop",new Document("$gte",String.valueOf(timestamp))));
    }

    @Override
    public FindIterable<Document> findDocumentByMinId(String collection,String id) {
        Document min = new Document();
        min.put("$gte",new ObjectId(id));
        return MongoDBConfig.getConnection().
                getCollection(collection).
                find(new Document("_id",min));
    }

    /**'
     * @Title: findAllDocument
     * @Description:  查询出全部的文档
     * @return: FindIterable<Document>
     */
    @Override
    public FindIterable<Document> findAllDocument(String collection) {
        return MongoDBConfig.getConnection().
                getCollection(collection).
                find();
    }

    /**
     * @Title: insertDocument
     * @Description:  新增 一条记录
     * @return: void
     */
    @Override
    public void insertDocument(String collection,Document document){
        MongoDBConfig.getConnection().
                getCollection(collection).
                insertOne(document);
    }

    /**
     * @Title: insertMultiDocument
     * @Description:  批量新增记录
     * @return: void
     */
    @Override
    public void insertMultiDocument(String collection,List<Document> documents){
        MongoDBConfig.getConnection().
                getCollection(collection).
                insertMany(documents);
    }

    /**
     * @Title: createCollection
     * @Description:  计数有多少文档
     * @return: void
     */
    @Override
    public long countDocument(String collection){
        return MongoDBConfig.getConnection().getCollection(collection).count();
    }

    @Override
    public void createCollection(String name) {
        MongoDBConfig.getConnection().createCollection(name);
    }

    @Override
    public void deleteCollection(String name) {
        MongoDBConfig.getConnection().getCollection(name).drop();
    }

    @Override
    public boolean isCollectionExist(String name) {
        MongoIterable<String> iterable = MongoDBConfig.getConnection().
                listCollectionNames();
        for(String string: iterable){
            if(string.equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> collections() {
        List<String> strings = new ArrayList<>();
        MongoIterable<String> mongoIterable = MongoDBConfig.getConnection().
                listCollectionNames();
        for(String string : mongoIterable){
            strings.add(string);
        }
        return strings;
    }
}

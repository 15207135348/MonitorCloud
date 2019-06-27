package com.whut.common.dao.mongodb;

import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.List;

public interface IMongoDBDao {

    //对一个集合的操作
    Document findDocument(String collection, String key, Object value);
    Document findLastDocument(String collection);
    FindIterable<Document> findLastMultiDocument(String collection,int count);
    FindIterable<Document> findAllDocument(String collection);
    FindIterable<Document> findDocumentByPage(String collection, int skip, int limit);
    FindIterable<Document> findDocumentByRange(String collection, String key, Object start, Object end);
    FindIterable<Document> findDocumentByMinId(String collection, String id);
    FindIterable<Document> findDocumentByMinId(String collection, String id, int limit);

    FindIterable<Document> findDocumentByTimestamp(String collection, long timestamp);

    void insertDocument(String collection, Document document);
    void insertMultiDocument(String collection, List<Document> documents);
    long countDocument(String collection);
    //对数据库的操作
    void createCollection(String name);
    void deleteCollection(String name);
    boolean isCollectionExist(String name);
    List<String> collections();







}

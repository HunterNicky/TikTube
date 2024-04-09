package com.aps.tiktube.model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Access<T extends Entity<T>>  {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> collection;
    private Class<T> type;

    public Access(Class<T> type) {
        this.client = MongoClients.create(
            MongoClientSettings.builder()
            .applyToConnectionPoolSettings(builder -> builder.maxSize(100))
            .applyConnectionString(new ConnectionString("mongodb://localhost:27017/?connectTimeoutMS=0&socketTimeoutMS=0"))
            .build());
        this.db = this.client.getDatabase("Tiktube");
        this.type = type;
        String[] subName = type.getName().split("\\.");
        this.collection = this.db.getCollection(subName[subName.length - 1]);
    }

    public Access(Class<T> type, String dbName) {
        String connectionString = "mongodb://localhost:27017/?connectTimeoutMS=0&socketTimeoutMS=0";
        this.client = MongoClients.create(connectionString);
        this.db = this.client.getDatabase(dbName);
        this.type = type;
        this.collection = null;
    }

    public Access(Class<T> type, String authString, String dbName) {
        String connectionString = authString + "?connectTimeoutMS=0&socketTimeoutMS=0";
        this.client = MongoClients.create(
            MongoClientSettings.builder()
            .applyToConnectionPoolSettings(builder -> builder.maxSize(100))
            .applyConnectionString(new ConnectionString(connectionString))
            .build());
        this.db = this.client.getDatabase(dbName);
        this.type = type;
        this.collection = null;
    }

    public String add(Entity<T> obj) {
        InsertOneResult result = collection.insertOne(obj.toDocument());
        return result.getInsertedId().asObjectId().getValue().toString();
    }

    public List<String> add(List<T> obj) {
        InsertManyResult result = collection
                .insertMany(obj.stream().map(Entity::toDocument).collect(Collectors.toList()));
        return result.getInsertedIds().values().stream().map(x -> x.asObjectId().getValue().toString())
                .collect(Collectors.toList());
    }

    public Document update(Entity<T> obj) {
        Document filter = new Document();
        ObjectId id = new ObjectId(obj.getId());
        filter.append("_id", id);
        Document updatedDocument = obj.toDocument();
        collection.replaceOne(filter, updatedDocument);
        return updatedDocument;
    }

    public static Document update(MongoCollection<Document> collection, Document updatedDocument) {
        String stringId = updatedDocument.get("_id").toString();

        ObjectId id = new ObjectId(stringId);
        Document filter = new Document();
        filter.append("_id", id);
        collection.replaceOne(filter, updatedDocument);
        
        return updatedDocument;
    }

    public void deleteById(String id) {
        Document filter = new Document();
        filter.append("_id", new ObjectId(id));
        this.collection.deleteOne(filter);
    }

    public void close() {
        this.client.close();
    }

    public List<T> where(String field, Object value) {
        Document filter = new Document();
        filter.append(field, value);
        FindIterable<Document> it = this.collection.find(filter);
        ArrayList<T> list = new ArrayList<>();
        MongoCursor<Document> cursor = it.cursor();
        while (cursor.hasNext()) {
            T obj = EntityFactory.New(type);
            obj.setFromDocument(cursor.next());
            obj.setExist(true);
            list.add(obj);
        }
        cursor.close();
        return list;
    }






}

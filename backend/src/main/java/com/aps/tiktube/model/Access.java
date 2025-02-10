package com.aps.tiktube.model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public class Access<T extends Entity<T>> {
    private final MongoClient client;
    private final MongoDatabase db;
    private final MongoCollection<Document> collection;
    private final Class<T> type;

    public Access(Class<T> type) {
        this.client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToConnectionPoolSettings(builder -> builder.maxSize(100))
                        .applyConnectionString(
                                new ConnectionString("mongodb://localhost:27017/?connectTimeoutMS=0&socketTimeoutMS=0"))
                        .build());
        this.db = this.client.getDatabase("Tiktube");
        this.type = type;
        String[] subName = type.getName().split("\\.");
        this.collection = this.db.getCollection(subName[subName.length - 1]);
    }

    public String add(Entity<T> obj) {
        InsertOneResult result = collection.insertOne(obj.toDocument());
        return Objects.requireNonNull(Objects.requireNonNull(result.getInsertedId())).asObjectId().getValue().toString();
    }

    public Document update(Entity<T> obj) {
        Document filter = new Document();
        ObjectId id = new ObjectId(obj.getId());
        filter.append("_id", id);
        Document updatedDocument = obj.toDocument();
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
        return getTs(it);
    }

    private List<T> getTs(FindIterable<Document> it) {
        ArrayList<T> list = new ArrayList<>();
        MongoCursor<Document> cursor = it.cursor();
        while (cursor.hasNext()) {
            T obj = EntityFactory.newEntity(type);
            obj.setFromDocument(cursor.next());
            obj.setExist(true);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<Document> getCollectionAsList(String collectionName) {
        try {
            MongoCollection<Document> newCollection = this.db.getCollection(collectionName);
            return newCollection.find().into(new ArrayList<>());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    
    public List<T> getCollectionAsListEnty(String collectionName) {
        try {
            MongoCollection<Document> newCollection = this.db.getCollection(collectionName);
            FindIterable<Document> it = newCollection.find();
            return getTs(it);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public T first(String field, Object value) {
        Document filter = new Document();
        filter.append(field, value);
        FindIterable<Document> it = this.collection.find(filter);
        MongoCursor<Document> cursor = it.cursor();
        T obj = EntityFactory.newEntity(type);
        if (cursor.hasNext()) {
            obj.setFromDocument(cursor.next());
            obj.setExist(true);
            cursor.close();
        }
        cursor.close();
        return obj;
    }

    public T getById(String id) {
        T obj = EntityFactory.newEntity(type);
        if (id != null && id.matches("^[0-9a-fA-F]+$")) {
            Document doc = null;
            Document filter = new Document();
            ObjectId idObject = new ObjectId(id);
            filter.append("_id", idObject);
            FindIterable<Document> it = this.collection.find(filter);
            MongoCursor<Document> cursor = it.cursor();
            if (cursor.hasNext())
                doc = cursor.next();

            if (doc != null) {
                obj.setFromDocument(doc);
                obj.setExist(true);
                cursor.close();
            }
            cursor.close();
        }
        return obj;
    }

    public List<T> where(List<String> field, List<Object> values) {
        Document filter = new Document();
        for (int i = 0; i < field.size() && i < values.size(); i++)
            filter.append(field.get(i), values.get(i));
        FindIterable<Document> it = this.collection.find(filter);
        return getTs(it);
    }

    public String importToGridFSFromInputStream(InputStream fileInputStream, String fileName, String fileType,
            String bucketName) throws IllegalArgumentException {
        validateInputParams(fileInputStream, fileName, fileType, bucketName);

        GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);
        return uploadFileToGridFS(gridFSBucket, fileName, fileInputStream, fileType);
    }

    private String uploadFileToGridFS(GridFSBucket gridFSBucket, String fileName, InputStream fileInputStream,
            String fileType) {
        try {
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1048576)
                    .metadata(new Document("type", fileType));
            ObjectId fileId = gridFSBucket.uploadFromStream(fileName, fileInputStream, options);
            return fileId.toHexString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void validateInputParams(InputStream fileInputStream, String fileName, String fileType,
            String bucketName) {
        if (fileInputStream == null)
            throw new IllegalArgumentException("Error importing file to db: fileInputStream cannot be null");
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("Error importing file to db: fileName cannot be empty.");
        if (fileType == null || fileType.isEmpty())
            throw new IllegalArgumentException("Error importing file to db: fileType cannot be empty.");
        if (bucketName == null || bucketName.isEmpty())
            throw new IllegalArgumentException("Error importing file to db: bucketName cannot be empty.");
    }

    private GridFSFile findGridFSFileById(GridFSBucket gridFSBucket, String id) {
        ObjectId objectId = new ObjectId(id);
        Bson filter = Filters.eq("_id", objectId);
        return gridFSBucket.find(filter).first();
    }

    /**
     * Returns the GridFsResource for streaming video
     *
     */
    public GridFsResource getMediaResource(String mediaId, String bucketName) {
        try {
            GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);
            GridFSFile gridFSFile = findGridFSFileById(gridFSBucket, mediaId);

            if (gridFSFile != null) {
                GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());

                return new GridFsResource(gridFSFile, gridFSDownloadStream);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

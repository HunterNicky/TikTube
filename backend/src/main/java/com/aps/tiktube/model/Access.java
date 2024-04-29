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
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

public class Access<T extends Entity<T>> {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> collection;
    private Class<T> type;

    static final String FILENAME = "filename";

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
            return newCollection.find().into(new ArrayList<Document>());
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

    public String importToGridFSFromInputStream(InputStream fileInputStream, String fileName, String fileType,
            String bucketName) throws IllegalArgumentException {
        validateInputParams(fileInputStream, fileName, fileType, bucketName);

        GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);
        return uploadFileToGridFS(gridFSBucket, fileName, fileInputStream, fileType);
    }

    public String importToGridFSFromInputStream(InputStream fileInputStream, String fileName, String fileType,
            String bucketName, String dbName) throws IllegalArgumentException {
        validateInputParams(fileInputStream, fileName, fileType, bucketName);

        GridFSBucket gridFSBucket = GridFSBuckets.create(this.client.getDatabase(dbName), bucketName);
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

    public InputStream getInputStreamGridFS(String videoId, String bucketName) {
        if (videoId == null || videoId.isEmpty()) {
            throw new IllegalArgumentException("Error retrieving file from db: id cannot be empty or null");
        }

        Bson filter = Filters.eq(FILENAME, videoId);
        GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);

        try {
            GridFSFile gridFSFile = gridFSBucket.find(filter).first();
            if (gridFSFile != null) {
                return gridFSBucket.openDownloadStream(gridFSFile.getId());
            } else {
                throw new FileNotFoundException("File not found in GridFS with id: " + videoId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Path getPathFromGridFS(String id, String bucketName, String path) {
        return getPathFromGridFS(id, bucketName, path, null);
    }

    public Path getPathFromGridFS(String id, String bucketName, String path, String dbName) {
        try {
            GridFSBucket gridFSBucket = (dbName != null && !dbName.isEmpty())
                    ? GridFSBuckets.create(client.getDatabase(dbName), bucketName)
                    : GridFSBuckets.create(this.db, bucketName);

            GridFSFile gridFSFile = findGridFSFileById(gridFSBucket, id);

            if (gridFSFile != null) {
                File file = new File(path + "/" + id + "." + gridFSFile.getMetadata().get("type"));
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    gridFSBucket.downloadToStream(new ObjectId(id), outputStream);
                    return file.toPath();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private GridFSFile findGridFSFileById(GridFSBucket gridFSBucket, String id) {
        ObjectId objectId = new ObjectId(id);
        Bson filter = Filters.eq("_id", objectId);
        return gridFSBucket.find(filter).first();
    }

    public void deleteFileFromGridFSByName(String videoId, String bucketName) {
        Bson filter = Filters.eq(FILENAME, videoId);
        try {
            GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);
            GridFSFile gridFSFile = gridFSBucket.find(filter).first();

            if (gridFSFile != null) {
                gridFSBucket.delete(gridFSFile.getObjectId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete file saved in GridFS
     * 
     * @param id
     * @param bucketName
     */
    public void deleteFileFromGridFS(String id, String bucketName) {
        if (id == null || id.equals(""))
            throw new IllegalArgumentException("Error retrieving ifle from db: id cannot be empty or null");
        ObjectId fileId = new ObjectId(id);
        GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);
        gridFSBucket.delete(fileId);
    }

    public void deleteFileFromGridFS(String id, String bucketName, String dbName) {
        if (id == null || id.equals(""))
            throw new IllegalArgumentException("Error retrieving ifle from db: id cannot be empty or null");
        ObjectId fileId = new ObjectId(id);
        GridFSBucket gridFSBucket = GridFSBuckets.create(this.client.getDatabase(dbName), bucketName);
        gridFSBucket.delete(fileId);
    }

    /**
     * Returns the GridFsResource for streaming video
     * 
     * @param mediaid
     * @param bucketName
     * @return
     */
    public GridFsResource getMediaResource(String mediaid, String bucketName) {
        Bson filter = Filters.eq(FILENAME, mediaid);
        try {
            GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);
            GridFSFile gridFSFile = gridFSBucket.find(filter).first();

            if (gridFSFile != null) {
                GridFSDownloadOptions options = new GridFSDownloadOptions()
                        .revision(gridFSFile.getMetadata().get("_id", 0));

                GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(mediaid, options);

                return new GridFsResource(gridFSFile, gridFSDownloadStream);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the type of file stored in GridFS
     * 
     * @param id
     * @param bucketName
     * @return
     */
    public String getGridFSFileType(String id, String bucketName) {
        if (id == null || id.equals(""))
            throw new IllegalArgumentException("Error retrieving file from db: id cannot be empty or null");
        ObjectId fileId = new ObjectId(id);
        GridFSBucket gridFSBucket = GridFSBuckets.create(this.db, bucketName);
        try {
            Bson query = Filters.eq("_id", fileId);
            return gridFSBucket.find(query)
                    .first()
                    .getMetadata()
                    .get("type")
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

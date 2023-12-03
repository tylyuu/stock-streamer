//package com.tylyuu.dataProcessor.services;
//
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoClient;
//import com.mongodb.MongoClientURI;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class DBWriter {
//    private static final String MONGO_DB_URL = "mongodb://127.0.0.1:27023";
//    private static final String DB_NAME = "stockdb";
//    private static final String COLLECTION_NAME = "movies";
//    MongoDatabase stockdb;
//    MongoCollection timeseriesCollection;
//    MongoCollection aggregatedDataCollection;
//
//
//    @Autowired
//    public DBWriter() {
//        MongoClient mongoClient = new MongoClient(new MongoClientURI(MONGO_DB_URL));
//    }
//
//    private static MongoDatabase connectToMongoDB(String url, String dbName) {
//        MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
//        return mongoClient.getDatabase(dbName);
//    }
//
//    // Pseudo-code for inserting the DataFrame
//    public void insertTimeSeriesData(Document doc) {
//        MongoCollection<Document> collection = database.getCollection("timeSeriesData");
//        collection.insertOne(doc);
//    }
//
//    // Pseudo-code for inserting the JSON Object
//    public void insertAggregatedMetrics(String json) {
//        MongoCollection<Document> collection = database.getCollection("aggregatedMetrics");
//        Document doc = Document.parse(json);
//        collection.insertOne(doc);
//    }
//
//
//}

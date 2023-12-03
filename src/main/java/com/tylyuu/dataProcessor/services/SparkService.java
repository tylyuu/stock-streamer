package com.tylyuu.dataProcessor.services;

import com.tylyuu.dataProcessor.utils.StockAnalysisHelper;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class SparkService {

    private JavaStreamingContext streamingContext;
    private SparkSession spark;
    private static final Logger logger = LoggerFactory.getLogger(SparkService.class);
    private static final String KAFKA_TOPIC = "output-topic";
    private static final String KAFKA_BROKER = "localhost:9092";
    private static final String GROUP_ID = "spark-kafka-group";
    private static int count = 0;

    private StockAnalysisHelper stockAnalysisHelper;

    public void start() {
        startSparkStreaming();
    }

    public void stop() {
        stopSparkStreaming();
    }

    private void startSparkStreaming() {
        SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName("KafkaSparkIntegration");
        this.spark = SparkSession.builder()
                .config(sparkConf)
        //        .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/database.collection")
                .config("spark.jars.packages", "org.mongodb.spark:mongo-spark-connector_2.11:2.3.2")
                .getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        streamingContext = new JavaStreamingContext(sc, Durations.seconds(10));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", KAFKA_BROKER);
        kafkaParams.put("key.deserializer", StringDeserializer.class.getName());
        kafkaParams.put("value.deserializer", StringDeserializer.class.getName());
        kafkaParams.put("group.id", GROUP_ID);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Set<String> topics = Collections.singleton(KAFKA_TOPIC);

        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
                streamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams)
        );


        stream.foreachRDD(rdd -> {
            JavaRDD<String> jsonRDD = rdd.map(ConsumerRecord::value);

            jsonRDD.foreach(jsonString -> logger.info("spark received " + jsonString));

            Dataset<Row> stockData = spark.read().json(jsonRDD);
//            logger.info("prev calculation: ");
//            stockData.show();
            stockData = StockAnalysisHelper.calculate(stockData, 1);
            logger.info("post calculation: ");
            Dataset<String> jsonStringDataset = stockData.toJSON();
            List<String> jsonList = jsonStringDataset.collectAsList();
        //    logger.info("json set: "+jsonList);
            writeListToJsonFile(jsonList, "/Users/lvtianyue/Downloads/data-processor/src/main/java/com/tylyuu/dataProcessor/output/samplesql"+count+".txt");
            count++;
//            stockData.write()
//                    .format("mongo") // use the MongoDB format
//                    .mode(SaveMode.Append) // specify the save mode, Append to add data
//                    .save(); // save the data
            String aggregatedData = StockAnalysisHelper.calculateAggregatedMetrics(stockData);
            logger.info("aggregated string: " + aggregatedData);
        });

        streamingContext.start();
        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            logger.error("Error in Spark Streaming", e);
        }
    }

    @PreDestroy
    public void stopSparkStreaming() {
        if (streamingContext != null) {
            streamingContext.stop(true, true);
            logger.info("Spark Streaming stopped");
        }
    }

    public static void writeListToJsonFile(List<String> jsonList, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)))) {
            for (String json : jsonList) {
                writer.write(json);
                writer.newLine(); // To ensure each JSON string is on a new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

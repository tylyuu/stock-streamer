package com.tylyuu.dataProcessor.services;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tylyuu.dataProcessor.config.JsonDeserializer;
import com.tylyuu.dataProcessor.message.Message;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.sql.Dataset;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import scala.Function1;
import scala.collection.JavaConversions;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Service
public class SparkService {

    private JavaStreamingContext streamingContext;
    private SparkSession spark;
    private static final Logger logger = LoggerFactory.getLogger(SparkService.class);
    private static final String KAFKA_TOPIC = "output-topic";
    private static final String KAFKA_BROKER = "localhost:9092";
    private static final String GROUP_ID = "spark-kafka-group";

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

            JavaRDD<Message> messageRDD = jsonRDD.mapPartitions(iterator -> {
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<Message> messages = new ArrayList<>();
                while (iterator.hasNext()) {
                    String json = iterator.next();
                    try {
                        Message message = mapper.readValue(json, Message.class);
                        if (message.getErrorMessage() == null || message.getErrorMessage().isEmpty()) {
                            messages.add(message);
                        } else {
                            logger.warn("Error Message from API: " + message.getErrorMessage());
                        }
                    } catch (Exception e) {
                        logger.error("Error parsing JSON", e);
                    }
                }
                return messages.iterator();
            });
            // Now you have an RDD of Message, convert it to DataFrame and perform calculations
            Dataset<Row> messageDataFrame = spark.createDataFrame(messageRDD, Message.class);

            // Perform your calculations here
            // For example, you can show the DataFrame
            messageDataFrame.show();
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

}

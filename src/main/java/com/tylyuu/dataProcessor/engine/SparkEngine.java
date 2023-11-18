package com.tylyuu.dataProcessor.engine;

import org.apache.kafka.common.serialization.StringDeserializer;
import com.tylyuu.dataProcessor.config.JsonDeserializer;
import com.tylyuu.dataProcessor.message.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SparkEngine {

    private JavaStreamingContext streamingContext;
    private static final Logger logger = LoggerFactory.getLogger(SparkEngine.class);

    // Initialize and start the Spark streaming context
    public void start() {
        SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName("KafkaSparkIntegration");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        streamingContext = new JavaStreamingContext(sc, Durations.seconds(10));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class.getName());
        kafkaParams.put("value.deserializer", StringDeserializer.class.getName());
        kafkaParams.put("group.id", "spark-kafka-group");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Set<String> topics = Collections.singleton("test_topic");

        // Using KafkaUtils.createDirectStream to create a direct stream from Kafka
        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(
                streamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams)
        );

        directStream.foreachRDD(rdd -> {
            // Processing logic for each RDD
            rdd.foreach(record -> {
                String json = record.value();
                Message message = JsonDeserializer.deserializeJson(json);
                if (message != null) {
                    // Process the message
                    logger.info("Spark Deserialized Message: " + message.getContent());
                }
            });
        });

        streamingContext.start();
        logger.info("Spark Streaming started");
    }

    public void stop() {
        if (streamingContext != null) {
            streamingContext.stop(true, true);
            logger.info("Spark Streaming stopped");
        }
    }
}

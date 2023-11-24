package com.tylyuu.dataProcessor.services;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.tylyuu.dataProcessor.config.JsonDeserializer;
import com.tylyuu.dataProcessor.message.Message;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class SparkService {

    private JavaStreamingContext streamingContext;
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
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
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

        stream.foreachRDD(rdd -> rdd.foreach(record -> {
            String json = record.value();
            logger.info("spark get json " + json);
            Message message = JsonDeserializer.deserializeJson(json);
            if (message != null) {
                // Process the message
                logger.info("Spark Deserialized Message from: " + message.getMetaData().getSymbol());
            }
        }));

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

package com.tylyuu.dataProcessor;

import com.tylyuu.dataProcessor.engine.KafkaEngine;
import com.tylyuu.dataProcessor.engine.SparkEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataProcessorApplication implements CommandLineRunner {

    private static final String TOPIC = "test_topic";
    private final Logger logger = LoggerFactory.getLogger(DataProcessorApplication.class);

    @Autowired
    KafkaEngine kafkaEngine;
    @Autowired
    SparkEngine sparkEngine;

    public static void main(String[] args) {
        SpringApplication.run(DataProcessorApplication.class, args);
    }

    public void run(String... args) throws Exception {
        try {
            logger.info("start processor...");
            kafkaEngine.start();
            sparkEngine.start();
        }
        finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    logger.info("System shutting down..." + Thread.currentThread().getName());
                    if (null != kafkaEngine) {
                        try {
                            kafkaEngine.stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    logger.info("System stopped");
                }
            });
        }

    }
}

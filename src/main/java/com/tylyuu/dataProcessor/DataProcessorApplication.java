package com.tylyuu.dataProcessor;

import com.tylyuu.dataProcessor.engine.ProcessorEngine;
import com.tylyuu.dataProcessor.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.CommandLineRunner;


@SpringBootApplication
@EnableScheduling
public class DataProcessorApplication implements CommandLineRunner{

    private static final String TOPIC = "test_topic";
    private final Logger logger = LoggerFactory.getLogger(DataProcessorApplication.class);

    @Autowired
    private ProcessorEngine processorEngine;

    public static void main(String[] args) {
        SpringApplication.run(DataProcessorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("start processor...");
            processorEngine.start();
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    logger.info("System shutting down..." + Thread.currentThread().getName());
                    if (null != processorEngine) {
                        try {
                            processorEngine.stop();
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

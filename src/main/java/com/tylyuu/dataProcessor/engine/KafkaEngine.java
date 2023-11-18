package com.tylyuu.dataProcessor.engine;

import com.tylyuu.dataProcessor.services.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaEngine {
    private static final Logger logger = LoggerFactory.getLogger(KafkaEngine.class);

    @Autowired
    ProducerService producerService;

    public void start() {
        producerService.start();
    }

    public void stop() {

    }
}

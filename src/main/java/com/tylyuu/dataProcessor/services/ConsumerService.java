package com.tylyuu.dataProcessor.services;

import com.tylyuu.dataProcessor.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "test_topic", groupId = "my_group_id")
    public void listen(Message message) {
        logger.info("Received Message in group my_group_id: " + message.getContent());
    }
}


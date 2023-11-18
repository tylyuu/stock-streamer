package com.tylyuu.dataProcessor.services;

import com.tylyuu.dataProcessor.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ProducerService {

    private static final String TOPIC = "test_topic";
    private final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public void sendMessage(Message message) {
        kafkaTemplate.send(
                MessageBuilder.withPayload(message)
                        .setHeader(KafkaHeaders.TOPIC, TOPIC)
                        .build()
        );
        logger.info("send " + message.getContent() + " to " + TOPIC);
    }

    @Scheduled(fixedRate = 5000) // Sends a message every 5 seconds
    public void sendScheduledMessage() {
        sendMessage(new Message("Hello at " + System.currentTimeMillis(), 1));
    }

    public void start() {
        sendScheduledMessage();
    }
}


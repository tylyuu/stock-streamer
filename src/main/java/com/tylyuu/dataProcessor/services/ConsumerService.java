package com.tylyuu.dataProcessor.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tylyuu.dataProcessor.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private int validLength = 7;

    @Autowired
    private ProducerService producerService;

    @KafkaListener(topics = "input-topic", groupId = "my_group_id")
    public void listen(String response) throws JsonProcessingException, IllegalAccessException {
        logger.info("Kafka consumer received Message in group my_group_id: " + response);
        Message message = convertStringToMessage(response);
        logger.info("Kafka consumer converted message with open " + message.getOpen());
        producerService.sendMessage(message);
    }
    public Message convertStringToMessage(String response) throws IllegalAccessException {
        String[] parts = response.split(",");
        if (parts.length != validLength) {
            throw new IllegalAccessException("Invalid data string " +response);
        }

        String date = parts[0];
        double open = Double.parseDouble(parts[1]);
        double high = Double.parseDouble(parts[2]);
        double low = Double.parseDouble(parts[3]);
        double close = Double.parseDouble(parts[4]);
        double adjustedClose = Double.parseDouble(parts[5]);
        long volume = Long.parseLong(parts[6]);

        return new Message(date, open, high, low, close, adjustedClose, volume);
    }
}


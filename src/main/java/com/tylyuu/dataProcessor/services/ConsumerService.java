package com.tylyuu.dataProcessor.services;

import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tylyuu.dataProcessor.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProducerService producerService;

    @KafkaListener(topics = "input-topic", groupId = "my_group_id")
    public void listen(String response) throws JsonProcessingException {
        logger.info("Kafka consumer received Message in group my_group_id: " + response.toString().substring(0,100));
        logger.info(response);
        Message message = convertStringToMessage(response);
  //      Message converted = convertStringToMessage(response);
        logger.info("Kafka consumer converted message from " + message.getMetaData().getSymbol());
//        producerService.sendMessage(converted);
    }
    public Message convertStringToMessage(String response) throws JsonProcessingException {
        Message message = objectMapper.readValue(response, Message.class);
        return message;
    }
}


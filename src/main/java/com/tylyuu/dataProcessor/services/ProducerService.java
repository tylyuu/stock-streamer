package com.tylyuu.dataProcessor.services;

import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.tylyuu.dataProcessor.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class ProducerService {

    private static final String INPUTTOPIC = "input-topic";
    private static final String OUTPUTOPIC = "output-topic";
    private final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Message> messageKafkaTemplate;

    public void sendStringMessage(String response) {
        try {
            ListenableFuture<SendResult<String, String>> future = stringKafkaTemplate.send(INPUTTOPIC, response);
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    logger.info("Sent message with offset=[" + result.getRecordMetadata().offset() + "]" + "in topic " + INPUTTOPIC);
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("Unable to send message due to : " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error serializing TimeSeriesResponse: " + e.getMessage());
        }
    }

    public void sendMessage(Message message) {
        try {
            ListenableFuture<SendResult<String, Message>> future = messageKafkaTemplate.send(OUTPUTOPIC, message);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Message>>() {
                @Override
                public void onSuccess(SendResult<String, Message> result) {
                    logger.info("Sent message with offset=[" + result.getRecordMetadata().offset() + "]" + "in topic " + OUTPUTOPIC);
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("Unable to send message due to : " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error serializing TimeSeriesResponse: " + e.getMessage());
        }
    }


}


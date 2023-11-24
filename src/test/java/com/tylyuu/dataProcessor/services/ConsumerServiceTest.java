package com.tylyuu.dataProcessor.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tylyuu.dataProcessor.message.Message;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerServiceTest {
    String testInput = "{\"metaData\":{\"information\":\"Intraday (5min) open, high, low, close prices and volume\",\"symbol\":\"IBM\",\"lastRefreshed\":\"2023-11-22 18:50:00\",\"timeZone\":\"US/Eastern\",\"interval\":\"5min\",\"outputSize\":\"Compact\"},\"stockUnits\":[{\"open\":155.05,\"high\":155.05,\"low\":155.05,\"close\":155.05,\"adjustedClose\":0.0,\"volume\":1,\"dividendAmount\":0.0,\"splitCoefficient\":0.0,\"date\":\"2023-11-22 18:50:00\"},{\"open\":155.07,\"high\":155.07,\"low\":155.07,\"close\":155.07,\"adjustedClose\":0.0,\"volume\":43,\"dividendAmount\":0.0,\"splitCoefficient\":0.0,\"date\":\"2023-11-22 18:45:00\"}]}";

    @Test
    public void testMessageConversion() throws JsonProcessingException {
        ConsumerService consumerService = new ConsumerService();
        Message message = consumerService.convertStringToMessage(testInput);
    }

}
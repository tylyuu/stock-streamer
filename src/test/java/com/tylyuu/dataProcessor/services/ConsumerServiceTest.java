package com.tylyuu.dataProcessor.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tylyuu.dataProcessor.message.Message;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerServiceTest {
    String testInput = "2022-11-28,143.199997,146.210007,140.580002,141.070007,141.070007,3523900";

    @Test
    public void testMessageConversion() throws IllegalAccessException {
        ConsumerService consumerService = new ConsumerService();
        Message message = consumerService.convertStringToMessage(testInput);
        assertEquals(message.getOpen(), 143.199997);
    }

}
package com.tylyuu.dataProcessor.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tylyuu.dataProcessor.message.Message;

public class JsonDeserializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Message deserializeJson(String json) {
        try {
            return objectMapper.readValue(json, Message.class);
        } catch (Exception e) {
            // Handle exception or return null
            return null;
        }
    }
}

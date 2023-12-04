package com.tylyuu.dataProcessor.services;

import com.tylyuu.dataProcessor.repository.StockDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;


class DBServiceTest {
    @Mock
    private StockDataRepository stockDataRepository;

    @InjectMocks
    private DBService dbService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsertJsonListIntoDb() {
        String jsonString = "{\"date\":[2022,12,7],\"adjustedClose\":137.410004,\"close\":137.410004,\"company\":\"snowflake\",\"high\":140.240005,\"low\":134.199997,\"open\":137.199997,\"volume\":5861200,\"movingAvg\":137.410004,\"priceVariation\":6.040008,\"simpleReturn\":0.49733637087211396}";
        List<String> jsonList = Collections.singletonList(jsonString);

        dbService.insertJsonListIntoDb(jsonList);

    }
}
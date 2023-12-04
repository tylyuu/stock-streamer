package com.tylyuu.dataProcessor.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tylyuu.dataProcessor.dbmodel.StockData;
import com.tylyuu.dataProcessor.repository.StockDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DBService {
    @Autowired
    private StockDataRepository stockDataRepository;
    private final Logger logger = LoggerFactory.getLogger(DBService.class);

    public void insertJsonListIntoDb(List<String> jsonList) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (String json : jsonList) {
            try {
                StockData stockData = objectMapper.readValue(json, StockData.class);
                logger.info("object converted");
                stockDataRepository.save(stockData);
                logger.info("data creation complete...");
            } catch (IOException e) {
                logger.error(String.valueOf(e));
            }
        }
    }



}

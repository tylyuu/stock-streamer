package com.tylyuu.dataProcessor.repository;

import com.tylyuu.dataProcessor.dbmodel.StockData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockDataRepository extends MongoRepository<StockData, String> {
    // Custom query methods can be defined here if needed
}
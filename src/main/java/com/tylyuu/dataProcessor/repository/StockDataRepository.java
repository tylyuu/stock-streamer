package com.tylyuu.dataProcessor.repository;

import com.tylyuu.dataProcessor.dbmodel.StockData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockDataRepository extends MongoRepository<StockData, String> { }
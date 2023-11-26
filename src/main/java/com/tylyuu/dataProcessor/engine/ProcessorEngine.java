package com.tylyuu.dataProcessor.engine;

import com.tylyuu.dataProcessor.services.AlphaVantageService;
import com.tylyuu.dataProcessor.services.RealTimeDataSimulator;
import com.tylyuu.dataProcessor.services.SparkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessorEngine {
    @Autowired
    private AlphaVantageService alphaVantageService;
    @Autowired
    private RealTimeDataSimulator realTimeDataSimulator;
    @Autowired
    private SparkService sparkService;

    private final Logger logger = LoggerFactory.getLogger(ProcessorEngine.class);

    public void start() {
        logger.info("Starting processor engine...");
        realTimeDataSimulator.start();
    //    alphaVantageService.start();
        sparkService.start();
    }

    public void stop() {
        realTimeDataSimulator.stop();
//        alphaVantageService.stop();
//        sparkService.stop();
    }
}

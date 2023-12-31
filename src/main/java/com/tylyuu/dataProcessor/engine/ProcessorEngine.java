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
    private final Logger logger = LoggerFactory.getLogger(ProcessorEngine.class);
    @Autowired
    private AlphaVantageService alphaVantageService;
    @Autowired
    private RealTimeDataSimulator realTimeDataSimulator;
    @Autowired
    private SparkService sparkService;

    public void start() throws InterruptedException {
        logger.info("Starting processor engine...");
        realTimeDataSimulator.start();
        Thread.sleep(1000);
        sparkService.start();
        // alphaVantageService.start();
    }

    public void stop() {
        realTimeDataSimulator.stop();
        alphaVantageService.stop();
        sparkService.stop();
    }
}

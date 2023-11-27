package com.tylyuu.dataProcessor.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
@EnableScheduling
public class RealTimeDataSimulator {
    private static String filePath = "/Users/lvtianyue/Downloads/data-processor/src/main/resources/data/SNOW.csv";
    private static final String INPUTTOPIC = "input-topic";
    private static final String OUTPUTOPIC = "output-topic";
    private BufferedReader bufferedReader;
    private final Logger logger = LoggerFactory.getLogger(RealTimeDataSimulator.class);
    private boolean isRunning = false;

    @Autowired
    private ProducerService producerService;

    public void start() {
        logger.info("starting simulator...");
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            // Skip the header line
            bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = true;
        sendFinanceData();
    }

    public void stop() {
        isRunning = false;
    }

    @Scheduled(fixedRate = 1000)
    public void sendFinanceData() {
        if(!isRunning) return;
        logger.info("simulator running");
        try {
            if (bufferedReader != null) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    producerService.sendStringMessage(line);
                    logger.info("simulator sent: " + line);
                } else {
                    // Consider logic for when end of file is reached
                    logger.info("End of file reached");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

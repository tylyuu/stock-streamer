package com.tylyuu.dataProcessor.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
@EnableScheduling
public class RealTimeDataSimulator {

    private final Logger logger = LoggerFactory.getLogger(RealTimeDataSimulator.class);
    @Value("${realtimedatasimulator.file-path}")
    private String filePath;
    @Value("${realtimedatasimulator.company}")
    private String company;
    @Value("${consumerservice.input-topic}")
    private String inputTopic;
    @Value("${consumerservice.output-topic}")
    private String outputTopic;
    private BufferedReader bufferedReader;
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
        if (!isRunning) return;
        logger.info("simulator running");
        try {
            if (bufferedReader != null) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String message = company + "," + line;
                    producerService.sendStringMessage(message);
                    logger.info("simulator sent: " + message);
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

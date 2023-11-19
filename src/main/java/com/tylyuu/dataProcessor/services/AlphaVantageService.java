package com.tylyuu.dataProcessor.services;

import com.tylyuu.dataProcessor.message.Message;
import com.tylyuu.dataProcessor.services.ProducerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class AlphaVantageService {

    @Autowired
    private ProducerService producerService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private boolean isRunning;

    @Autowired
    public AlphaVantageService(ProducerService producerService) {
        this.producerService = producerService;
        Config cfg = Config.builder()
                .key("2X798415XIXS2CCF") // Replace with your API key
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
        fetchAndSendStockData();
    }

    public void stop() {
        isRunning = false;
    }

    public static TimeSeriesResponse fetchIntradayStockData(String symbol) {
        try {
            return AlphaVantage.api()
                    .timeSeries()
                    .intraday()
                    .forSymbol(symbol)
                    .interval(Interval.FIVE_MIN)
                    .outputSize(OutputSize.COMPACT)
                    .fetchSync();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Scheduled(fixedRate = 5000) // Adjust the rate as needed
    public void fetchAndSendStockData() {
        if(!isRunning) return;

        TimeSeriesResponse response = fetchIntradayStockData("IBM");
        if (response != null) {
            try {
                String json = objectMapper.writeValueAsString(response);
                producerService.sendMessage(new Message(json, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static TimeSeriesResponse fetchDailyStockData(String symbol) {
        try {
            return AlphaVantage.api()
                    .timeSeries()
                    .daily()
                    .forSymbol(symbol)
                    .outputSize(OutputSize.FULL)
                    .fetchSync();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        Config cfg = Config.builder()
                .key("2X798415XIXS2CCF") // Replace with your API key
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
    }


    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("/Users/lvtianyue/Downloads/data-processor/src/main/java/com/tylyuu/dataProcessor/output/sampleStockData.txt");
        writer.write(fetchIntradayStockData("IBM").toString());
        writer.close();
        return;
    }








}

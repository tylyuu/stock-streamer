package com.tylyuu.dataProcessor.services;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AlphaVantageService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${alphavantage.apikey}")
    private static String APIKEY;

    @Value("${alphavantage.company}")
    private static String company;

    static {
        Config cfg = Config.builder()
                .key(APIKEY) // Replace with your API key
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
    }

    private final Logger logger = LoggerFactory.getLogger(AlphaVantageService.class);

    @Autowired
    private ProducerService producerService;
    private boolean isRunning;

    @Autowired
    public AlphaVantageService(ProducerService producerService) {
        this.producerService = producerService;
        Config cfg = Config.builder()
                .key(APIKEY) // Replace with your API key
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
        this.isRunning = false;
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


    public void start() {
        isRunning = true;
        fetchAndSendStockData();
    }

    public void stop() {
        isRunning = false;
    }

    @Scheduled(fixedRate = 5000) // Adjust the rate as needed
    public void fetchAndSendStockData() {
        if (!isRunning) return;

        TimeSeriesResponse response = fetchIntradayStockData("IBM");
        if (response != null) {
            try {
                String json = objectMapper.writeValueAsString(response);
                logger.info(response.toString().substring(0, 100));
                logger.info("alphavantage sending " + json);
                producerService.sendStringMessage(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

package com.tylyuu.dataProcessor.services;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.crazzyghost.alphavantage.timeseries.TimeSeries;
import com.tylyuu.dataProcessor.DataProcessorApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

@Service
public class AlphaVantageService {

    public AlphaVantageService() {
        Config cfg = Config.builder()
                .key("2X798415XIXS2CCF") // Replace with your API key
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
    }

    static {
        Config cfg = Config.builder()
                .key("2X798415XIXS2CCF") // Replace with your API key
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
    }

    public static TimeSeriesResponse fetchIntradayStockData(String symbol) {
        try {
            return AlphaVantage.api()
                    .timeSeries()
                    .intraday()
                    .forSymbol(symbol)
                    .interval(Interval.FIVE_MIN)
                    .outputSize(OutputSize.FULL)
                    .fetchSync();
        } catch (Exception e) {
            // Handle exceptions (network issues, API limit exceeded, etc.)
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        System.out.println(fetchIntradayStockData("IBM"));
    }



    // You can add additional methods to handle other types of data requests
}

package com.tylyuu.dataProcessor.message;

import com.crazzyghost.alphavantage.timeseries.response.MetaData;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Message {
    @JsonProperty("metaData")
    private MetaData metaData;
    @JsonProperty("stockUnits")
    private List<StockUnit> stockUnits;
    @JsonProperty("errorMessage")
    private String errorMessage;

    public MetaData getMetaData() {
        return metaData;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MetaData {
        private String information;
        private String symbol;
        private String lastRefreshed;
        private String timeZone;
        private String interval;
        private String outputSize;

        // Getter and setter for information
        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        // Getter and setter for symbol
        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getLastRefreshed() {
            return lastRefreshed;
        }

        public void setLastRefreshed(String lastRefreshed) {
            this.lastRefreshed = lastRefreshed;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public String getOutputSize() {
            return outputSize;
        }

        public void setOutputSize(String outputSize) {
            this.outputSize = outputSize;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StockUnit {
        private double open;
        private double high;
        private double low;
        private double close;
        private double adjustedClose;
        private int volume;
        private double dividendAmount;
        private double splitCoefficient;
        private String date;

        // Getter and Setter for open
        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        // Getter and Setter for high
        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        // Getter and Setter for low
        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        // Getter and Setter for close
        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        // Getter and Setter for adjustedClose
        public double getAdjustedClose() {
            return adjustedClose;
        }

        public void setAdjustedClose(double adjustedClose) {
            this.adjustedClose = adjustedClose;
        }

        // Getter and Setter for volume
        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        // Getter and Setter for dividendAmount
        public double getDividendAmount() {
            return dividendAmount;
        }

        public void setDividendAmount(double dividendAmount) {
            this.dividendAmount = dividendAmount;
        }

        // Getter and Setter for splitCoefficient
        public double getSplitCoefficient() {
            return splitCoefficient;
        }

        public void setSplitCoefficient(double splitCoefficient) {
            this.splitCoefficient = splitCoefficient;
        }

        // Getter and Setter for date
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }


}

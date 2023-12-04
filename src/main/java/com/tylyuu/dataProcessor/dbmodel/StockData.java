package com.tylyuu.dataProcessor.dbmodel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("stockdata")
public class StockData {

    @Id
    private String id;
    private List<Integer> date;
    private Double adjustedClose;
    private Double close;
    private String company;
    private Double high;

    public Double getSimpleReturn() {
        return simpleReturn;
    }

    public void setSimpleReturn(Double simpleReturn) {
        this.simpleReturn = simpleReturn;
    }

    public StockData(String id, List<Integer> date, Double adjustedClose, Double close, String company, Double high, Double low, Double open, Double simpleReturn, Integer volume, Double movingAvg, Double priceVariation) {
        this.id = id;
        this.date = date;
        this.adjustedClose = adjustedClose;
        this.close = close;
        this.company = company;
        this.high = high;
        this.low = low;
        this.open = open;
        this.simpleReturn = simpleReturn;
        this.volume = volume;
        this.movingAvg = movingAvg;
        this.priceVariation = priceVariation;
    }

    private Double low;
    private Double open;
    private Double simpleReturn;

    public StockData() {
        // Default constructor
    }



    private Integer volume;
    private Double movingAvg;
    private Double priceVariation;


    public Double getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(Double adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getMovingAvg() {
        return movingAvg;
    }

    public Double getPriceVariation() {
        return priceVariation;
    }

    public void setPriceVariation(Double priceVariation) {
        this.priceVariation = priceVariation;
    }

    public void setMovingAvg(Double movingAvg) {
        this.movingAvg = movingAvg;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getClose() {
        return close;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public List<Integer> getDate() {
        return date;
    }

    public void setDate(List<Integer> date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}

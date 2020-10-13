package com.aj.options.entity;

import java.util.Date;

public class Option {
    long expirationDate;
    long lastTradeDate;
    String type;  //call or put
    String symbol;
    double strike;
    double lastPrice;
    double bid;
    double ask;
    double iv;
    boolean isItm;
    double modelValue;
    double ulPrice;
    double price;

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public boolean isItm() {
        return isItm;
    }

    public void setItm(boolean itm) {
        isItm = itm;
    }

    public double getIv() {
        return iv;
    }

    public void setIv(double iv) {
        this.iv = iv;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getModelValue() {
        return modelValue;
    }

    public void setModelValue(double modelValue) {
        this.modelValue = modelValue;
    }

    public double getUlPrice() {
        return ulPrice;
    }

    public void setUlPrice(double ulPrice) {
        this.ulPrice = ulPrice;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public long getLastTradeDate() {
        return lastTradeDate;
    }

    public void setLastTradeDate(long lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    @Override
    public String toString() {
        return "Option{" +
                "expirationDate=" + expirationDate +
                ", type='" + type + '\'' +
                ", symbol='" + symbol + '\'' +
                ", strike=" + strike +
                ", lastPrice=" + lastPrice +
                ", bid=" + bid +
                ", ask=" + ask +
                ", iv=" + iv +
                ", isItm=" + isItm +
                '}';
    }
}

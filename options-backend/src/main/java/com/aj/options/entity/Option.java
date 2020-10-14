package com.aj.options.entity;

import lombok.Data;

import java.util.Date;

@Data
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
}

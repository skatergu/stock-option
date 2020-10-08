package com.aj.options.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OptionChain {
    private Stock ulStock;
 //   private List<Long> expirationDates;
    private List<Option> allOptions;


    public List<Option> getAllOptions() {
        return allOptions;
    }

    public void setAllOptions(List<Option> allOptions) {
        this.allOptions = allOptions;
    }

    public Stock getUlStock() {
        return ulStock;
    }

    public void setUlStock(Stock ulStock) {
        this.ulStock = ulStock;
    }

    @Override
    public String toString() {
        return "OptionChain{" +
                "ulStock=" + ulStock +
              //  ", expirationDates=" + expirationDates +
                ", allOptions=" + allOptions +
                '}';
    }
}

package com.test.aquariux.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PriceDTO implements Serializable {
    private String currencyPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }
}

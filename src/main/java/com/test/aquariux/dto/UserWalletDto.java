package com.test.aquariux.dto;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class UserWalletDto implements Serializable {
    private Long id;
    private String currency;
    private BigDecimal balance;
    private List<Map<String, BigDecimal>> holdings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Map<String, BigDecimal>> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Map<String, BigDecimal>> holdings) {
        this.holdings = holdings;
    }
}
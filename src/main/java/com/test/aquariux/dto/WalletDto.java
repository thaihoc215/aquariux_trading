package com.test.aquariux.dto;

import java.io.Serializable;
import java.math.BigDecimal;


public record WalletDto(Long id, String currency, BigDecimal balance) implements Serializable {
}
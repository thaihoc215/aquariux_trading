package com.test.aquariux.service;

import com.test.aquariux.dto.TradeDTO;
import com.test.aquariux.dto.TradingRequest;
import com.test.aquariux.dto.TradingResponse;

import java.util.List;

public interface TradeService {
    TradingResponse executeTrade(TradingRequest tradingRequest);
    List<TradeDTO> getUserTradingHistory(Long userId);
}
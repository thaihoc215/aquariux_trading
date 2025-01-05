package com.test.aquariux.controller;

import com.test.aquariux.dto.TradeDTO;
import com.test.aquariux.dto.TradingRequest;
import com.test.aquariux.dto.TradingResponse;
import com.test.aquariux.service.TradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping
    public TradingResponse executeTrade(@RequestBody TradingRequest tradingRequest) {
        return tradeService.executeTrade(tradingRequest);
    }

    @GetMapping("/trading-history")
    public List<TradeDTO> getUserTradingHistory(@RequestParam Long userId) {
        return tradeService.getUserTradingHistory(userId);
    }
}
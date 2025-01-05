package com.test.aquariux.controller;

import com.test.aquariux.dto.TradeDTO;
import com.test.aquariux.dto.TradingRequest;
import com.test.aquariux.dto.TradingResponse;
import com.test.aquariux.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing trade-related operations.
 */
@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * Executes a trade based on the provided trading request.
     *
     * @param tradingRequest the trading request containing trade details
     * @return the response of the trade execution as a TradingResponse
     */
    @PostMapping
    public TradingResponse executeTrade(@RequestBody @Valid TradingRequest tradingRequest) {
        return tradeService.executeTrade(tradingRequest);
    }

    /**
     * Retrieves the trading history for a specific user.
     *
     * @param userId the ID of the user whose trading history is to be retrieved
     * @return a list of TradeDTO representing the user's trading history
     */
    @GetMapping("/trading-history")
    public List<TradeDTO> getUserTradingHistory(@RequestParam Long userId) {
        return tradeService.getUserTradingHistory(userId);
    }
}
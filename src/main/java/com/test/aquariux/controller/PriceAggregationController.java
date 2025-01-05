package com.test.aquariux.controller;

import com.test.aquariux.dto.PriceDTO;
import com.test.aquariux.service.PriceAggregationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing price aggregation operations.
 */
@RestController
@RequestMapping("/api/prices")
public class PriceAggregationController {

    private final PriceAggregationService priceAggregationService;

    public PriceAggregationController(PriceAggregationService priceAggregationService) {
        this.priceAggregationService = priceAggregationService;
    }

    /**
     * Get the latest best aggregated price for a given currency pair.
     * @param currencyPair The currency pair to get the price for.
     * @return The latest best aggregated price for the given currency pair.
     */
    @GetMapping("/latest")
    public PriceDTO getLatestBestAggregatedPrice(@RequestParam String currencyPair) {
        return priceAggregationService.getLatestBestAggregatedPrice(currencyPair);
    }
}
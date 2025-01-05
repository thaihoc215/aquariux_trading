package com.test.aquariux.controller;

import com.test.aquariux.dto.PriceDTO;
import com.test.aquariux.service.PriceAggregationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
public class PriceAggregationController {

    private final PriceAggregationService priceAggregationService;

    public PriceAggregationController(PriceAggregationService priceAggregationService) {
        this.priceAggregationService = priceAggregationService;
    }

//    @GetMapping("/aggregate")
//    public void aggregatePrices() {
//        priceAggregationService.aggregatePrices();
//    }

    @GetMapping("/latest")
    public PriceDTO getLatestBestAggregatedPrice(@RequestParam String currencyPair) {
        return priceAggregationService.getLatestBestAggregatedPrice(currencyPair);
    }
}
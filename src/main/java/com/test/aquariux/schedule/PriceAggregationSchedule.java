package com.test.aquariux.schedule;

import com.test.aquariux.service.PriceAggregationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceAggregationSchedule {

    private final PriceAggregationService priceAggregationService;

    public PriceAggregationSchedule(PriceAggregationService priceAggregationService) {
        this.priceAggregationService = priceAggregationService;
    }

    @Scheduled(fixedRate = 10000)
    public void schedulePriceAggregation() {
        priceAggregationService.aggregatePrices();
    }
}
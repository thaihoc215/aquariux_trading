package com.test.aquariux.service;

import com.test.aquariux.dto.PriceDTO;

public interface PriceAggregationService {
    void aggregatePrices();
    PriceDTO getLatestBestAggregatedPrice(String currencyPair);
}

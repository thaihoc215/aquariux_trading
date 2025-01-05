package com.test.aquariux.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.aquariux.dto.PriceDTO;
import com.test.aquariux.entity.Price;
import com.test.aquariux.repository.PriceRepository;
import com.test.aquariux.service.PriceAggregationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PriceAggregationServiceImpl implements PriceAggregationService {

    private final PriceRepository priceRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker?symbols=[\"BTCUSDT\",\"ETHUSDT\"]";
    private static final String HUOBI_URL = "https://api.huobi.pro/market/tickers";

    public PriceAggregationServiceImpl(PriceRepository priceRepository, ObjectMapper objectMapper) {
        this.priceRepository = priceRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    @Override
    public void aggregatePrices() {
        Map<String, BigDecimal> binancePrices = fetchPricesFromBinance();
        Map<String, BigDecimal> huobiPrices = fetchPricesFromHuobi();
        String[] pairs = {"ETHUSDT", "BTCUSDT"};

        for (String pair : pairs) {
            BigDecimal binanceBid = binancePrices.get(pair + "_bid");
            BigDecimal binanceAsk = binancePrices.get(pair + "_ask");
            BigDecimal huobiBid = huobiPrices.get(pair + "_bid");
            BigDecimal huobiAsk = huobiPrices.get(pair + "_ask");

            BigDecimal bestBid = binanceBid.max(huobiBid);
            BigDecimal bestAsk = binanceAsk.min(huobiAsk);

            Price price = priceRepository.findTopByCurrencyPairOrderByCreatedAtDesc(pair)
                    .orElse(new Price());

            price.setCurrencyPair(pair);
            price.setBidPrice(bestBid);
            price.setAskPrice(bestAsk);
            price.setCreatedAt(price.getCreatedAt() == null ? LocalDateTime.now() : price.getCreatedAt());
            price.setUpdatedAt(LocalDateTime.now());

            priceRepository.save(price);
        }
    }

    private Map<String, BigDecimal> fetchPricesFromBinance() {
        Map<String, BigDecimal> prices = new HashMap<>();
        Map[] response = restTemplate.getForObject(BINANCE_URL, Map[].class);

        if (response != null) {
            for (Map<String, String> ticker : response) {
                String symbol = ticker.get("symbol");
                if ("ETHUSDT".equals(symbol) || "BTCUSDT".equals(symbol)) {
                    prices.put(symbol + "_bid", new BigDecimal(ticker.get("bidPrice")));
                    prices.put(symbol + "_ask", new BigDecimal(ticker.get("askPrice")));
                }
            }
        }
        return prices;
    }

    private Map<String, BigDecimal> fetchPricesFromHuobi() {
        Map<String, BigDecimal> prices = new HashMap<>();
        Map<String, Object> response = restTemplate.getForObject(HUOBI_URL, Map.class);

        if (response != null && "ok".equals(response.get("status"))) {
            List<Map<String, Object>> tickers = (List<Map<String, Object>>) response.get("data");
            for (Map<String, Object> ticker : tickers) {
                String symbol = (String) ticker.get("symbol");
                if ("ethusdt".equals(symbol) || "btcusdt".equals(symbol)) {
                    prices.put(symbol.toUpperCase() + "_bid", new BigDecimal(ticker.get("bid").toString()));
                    prices.put(symbol.toUpperCase() + "_ask", new BigDecimal(ticker.get("ask").toString()));
                }
            }
        }
        return prices;
    }

    @Override
    public PriceDTO getLatestBestAggregatedPrice(String currencyPair) {
        Price price = priceRepository.findTopByCurrencyPairOrderByCreatedAtDesc(currencyPair)
                .orElseThrow(() -> new IllegalArgumentException("No latest price available for the currency pair: " + currencyPair));
        return objectMapper.convertValue(price, PriceDTO.class);

    }
}

package com.test.aquariux.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.aquariux.dto.PriceDTO;
import com.test.aquariux.entity.Price;
import com.test.aquariux.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PriceAggregationServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PriceAggregationServiceImpl priceAggregationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLatestBestAggregatedPrice() {
        String currencyPair = "ETHUSDT";
        Price price = new Price();
        price.setCurrencyPair(currencyPair);
        price.setBidPrice(BigDecimal.valueOf(3000));
        price.setAskPrice(BigDecimal.valueOf(3100));

        when(priceRepository.findTopByCurrencyPairOrderByCreatedAtDesc(currencyPair)).thenReturn(Optional.of(price));

        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setCurrencyPair(currencyPair);
        priceDTO.setBidPrice(BigDecimal.valueOf(3000));
        priceDTO.setAskPrice(BigDecimal.valueOf(3100));

        when(objectMapper.convertValue(price, PriceDTO.class)).thenReturn(priceDTO);

        PriceDTO result = priceAggregationService.getLatestBestAggregatedPrice(currencyPair);

        assertNotNull(result);
        assertEquals(currencyPair, result.getCurrencyPair());
        assertEquals(BigDecimal.valueOf(3000), result.getBidPrice());
        assertEquals(BigDecimal.valueOf(3100), result.getAskPrice());

        verify(priceRepository, times(1)).findTopByCurrencyPairOrderByCreatedAtDesc(currencyPair);
    }
}
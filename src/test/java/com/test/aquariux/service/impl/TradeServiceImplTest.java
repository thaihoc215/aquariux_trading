package com.test.aquariux.service.impl;

import com.test.aquariux.dto.PriceDTO;
import com.test.aquariux.dto.TradingRequest;
import com.test.aquariux.dto.TradingResponse;
import com.test.aquariux.entity.Trade;
import com.test.aquariux.entity.User;
import com.test.aquariux.entity.Wallet;
import com.test.aquariux.enums.TradeType;
import com.test.aquariux.repository.CryptoHoldingRepository;
import com.test.aquariux.repository.TradeRepository;
import com.test.aquariux.repository.WalletRepository;
import com.test.aquariux.service.PriceAggregationService;
import com.test.aquariux.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeServiceImplTest {

    @Mock
    private PriceAggregationService priceAggregationService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private UserService userService;

    @Mock
    private CryptoHoldingRepository cryptoHoldingRepository;

    @InjectMocks
    private TradeServiceImpl tradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteTrade() {
        TradingRequest tradingRequest = new TradingRequest();
        tradingRequest.setUserId(1L);
        tradingRequest.setCurrencyPair("ETHUSDT");
        tradingRequest.setTradeType("BUY");
        tradingRequest.setAmount(BigDecimal.valueOf(1.5));

        User user = new User();
        user.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setCurrency("USDT");
        wallet.setBalance(BigDecimal.valueOf(50000));

        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setCurrencyPair("ETHUSDT");
        priceDTO.setAskPrice(BigDecimal.valueOf(3000));
        priceDTO.setBidPrice(BigDecimal.valueOf(2900));

        Trade savedTrade = new Trade();
        savedTrade.setId(1L);
        savedTrade.setTradeType(TradeType.BUY);
        savedTrade.setCurrencyPair("ETHUSDT");
        savedTrade.setPrice(BigDecimal.valueOf(3000));
        savedTrade.setAmount(BigDecimal.valueOf(1.5));
        savedTrade.setCreatedAt(LocalDateTime.now());

        when(userService.getUserById(1L)).thenReturn(user);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        when(priceAggregationService.getLatestBestAggregatedPrice("ETHUSDT")).thenReturn(priceDTO);
        when(tradeRepository.save(any(Trade.class))).thenReturn(savedTrade);

        TradingResponse tradingResponse = tradeService.executeTrade(tradingRequest);

        assertNotNull(tradingResponse);
        assertEquals("ETHUSDT", tradingResponse.getCurrencyPair());
        assertEquals(BigDecimal.valueOf(3000), tradingResponse.getPrice());
        assertEquals(BigDecimal.valueOf(1.5), tradingResponse.getAmount());
        assertEquals(0, tradingResponse.getTotal().compareTo(BigDecimal.valueOf(4500)));

        verify(userService, times(1)).getUserById(1L);
        verify(walletRepository, times(1)).findByUserId(1L);
        verify(priceAggregationService, times(1)).getLatestBestAggregatedPrice("ETHUSDT");
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }
}
package com.test.aquariux.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.aquariux.dto.UserWalletDto;
import com.test.aquariux.dto.WalletDto;
import com.test.aquariux.entity.CryptoHolding;
import com.test.aquariux.entity.Wallet;
import com.test.aquariux.repository.CryptoHoldingRepository;
import com.test.aquariux.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CryptoHoldingRepository cryptoHoldingRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserWallets() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setCurrency("USDT");
        wallet.setBalance(BigDecimal.valueOf(50000));

        CryptoHolding holding = new CryptoHolding();
        holding.setCryptoCurrency("ETH");
        holding.setAmount(BigDecimal.valueOf(10));

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        when(cryptoHoldingRepository.findByWallet(wallet)).thenReturn(List.of(holding));

        UserWalletDto userWalletDto = new UserWalletDto();
        userWalletDto.setId(1L);
        userWalletDto.setCurrency("USDT");
        userWalletDto.setBalance(BigDecimal.valueOf(50000));
        userWalletDto.setHoldings(List.of(Map.of("ETH", BigDecimal.valueOf(10))));

        when(objectMapper.convertValue(wallet, UserWalletDto.class)).thenReturn(userWalletDto);

        UserWalletDto result = walletService.getUserWallets(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("USDT", result.getCurrency());
        assertEquals(BigDecimal.valueOf(50000), result.getBalance());
        assertEquals(1, result.getHoldings().size());
        assertEquals(BigDecimal.valueOf(10), result.getHoldings().get(0).get("ETH"));

        verify(walletRepository, times(1)).findByUserId(1L);
        verify(cryptoHoldingRepository, times(1)).findByWallet(wallet);
        verify(objectMapper, times(1)).convertValue(wallet, UserWalletDto.class);
    }

    @Test
    void testFindByUserId() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setCurrency("USDT");
        wallet.setBalance(BigDecimal.valueOf(50000));

        WalletDto walletDto = new WalletDto(1L, "USDT", BigDecimal.valueOf(50000));

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        when(objectMapper.convertValue(wallet, WalletDto.class)).thenReturn(walletDto);

        WalletDto result = walletService.findByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("USDT", result.currency());
        assertEquals(BigDecimal.valueOf(50000), result.balance());

        verify(walletRepository, times(1)).findByUserId(1L);
        verify(objectMapper, times(1)).convertValue(wallet, WalletDto.class);
    }
}
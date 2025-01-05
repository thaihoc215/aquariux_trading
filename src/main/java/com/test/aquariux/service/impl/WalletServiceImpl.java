package com.test.aquariux.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.aquariux.dto.UserWalletDto;
import com.test.aquariux.dto.WalletDto;
import com.test.aquariux.entity.CryptoHolding;
import com.test.aquariux.entity.Wallet;
import com.test.aquariux.repository.CryptoHoldingRepository;
import com.test.aquariux.repository.WalletRepository;
import com.test.aquariux.service.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CryptoHoldingRepository cryptoHoldingRepository;
    private final ObjectMapper objectMapper;

    public WalletServiceImpl(WalletRepository walletRepository, CryptoHoldingRepository cryptoHoldingRepository, ObjectMapper objectMapper) {
        this.walletRepository = walletRepository;
        this.cryptoHoldingRepository = cryptoHoldingRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserWalletDto getUserWallets(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User wallet not found"));

        List<CryptoHolding> holdings = cryptoHoldingRepository.findByWallet(wallet);
        List<Map<String, BigDecimal>> holdingsDto = holdings.stream()
                .map(holding -> Map.of(holding.getCryptoCurrency(), holding.getAmount()))
                .collect(Collectors.toList());

        UserWalletDto userWalletDto = objectMapper.convertValue(wallet, UserWalletDto.class);
        userWalletDto.setHoldings(holdingsDto);

        return userWalletDto;
    }

    @Override
    public WalletDto findByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User wallet not found"));
        return objectMapper.convertValue(wallet, WalletDto.class);
    }
}
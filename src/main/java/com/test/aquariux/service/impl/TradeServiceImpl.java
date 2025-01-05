package com.test.aquariux.service.impl;

import com.test.aquariux.dto.PriceDTO;
import com.test.aquariux.dto.TradeDTO;
import com.test.aquariux.dto.TradingRequest;
import com.test.aquariux.dto.TradingResponse;
import com.test.aquariux.entity.CryptoHolding;
import com.test.aquariux.entity.Trade;
import com.test.aquariux.entity.User;
import com.test.aquariux.entity.Wallet;
import com.test.aquariux.enums.TradeType;
import com.test.aquariux.repository.CryptoHoldingRepository;
import com.test.aquariux.repository.TradeRepository;
import com.test.aquariux.repository.WalletRepository;
import com.test.aquariux.service.PriceAggregationService;
import com.test.aquariux.service.TradeService;
import com.test.aquariux.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    private final PriceAggregationService priceAggregationService;
    private final WalletRepository walletRepository;
    private final TradeRepository tradeRepository;
    private final UserService userService;
    private final CryptoHoldingRepository cryptoHoldingRepository;

    public TradeServiceImpl(PriceAggregationService priceAggregationService, WalletRepository walletRepository,
                            TradeRepository tradeRepository, UserService userService, CryptoHoldingRepository cryptoHoldingRepository) {
        this.priceAggregationService = priceAggregationService;
        this.walletRepository = walletRepository;
        this.tradeRepository = tradeRepository;
        this.userService = userService;
        this.cryptoHoldingRepository = cryptoHoldingRepository;
    }

    @Override
    @Transactional
    public TradingResponse executeTrade(TradingRequest tradingRequest) {
        User user = userService.getUserById(tradingRequest.getUserId());
        String currencyPair = tradingRequest.getCurrencyPair();
        String tradeType = tradingRequest.getTradeType();
        BigDecimal amount = tradingRequest.getAmount();

        PriceDTO latestPrice = priceAggregationService.getLatestBestAggregatedPrice(currencyPair);
        if (latestPrice == null) {
            throw new IllegalArgumentException("No latest price available for the currency pair: " + currencyPair);
        }

        Wallet wallet = walletRepository.findByUserAndCurrency(user, "USDT");
        if (wallet == null) {
            throw new IllegalArgumentException("User does not have a USDT wallet");
        }

        BigDecimal price = tradeType.equalsIgnoreCase("BUY") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();
        BigDecimal totalCost = price.multiply(amount);

        if (tradeType.equalsIgnoreCase("BUY")) {
            if (wallet.getBalance().compareTo(totalCost) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            wallet.setBalance(wallet.getBalance().subtract(totalCost));

            CryptoHolding cryptoHolding = cryptoHoldingRepository.findByWalletAndCryptoCurrency(wallet, currencyPair.substring(0, 3));
            if (cryptoHolding == null) {
                cryptoHolding = new CryptoHolding();
                cryptoHolding.setWallet(wallet);
                cryptoHolding.setCryptoCurrency(currencyPair.substring(0, 3));
                cryptoHolding.setAmount(amount);
            } else {
                cryptoHolding.setAmount(cryptoHolding.getAmount().add(amount));
            }
            cryptoHoldingRepository.save(cryptoHolding);

        } else if (tradeType.equalsIgnoreCase("SELL")) {
            CryptoHolding cryptoHolding = cryptoHoldingRepository.findByWalletAndCryptoCurrency(wallet, currencyPair.substring(0, 3));
            if (cryptoHolding == null || cryptoHolding.getAmount().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient crypto balance");
            }
            cryptoHolding.setAmount(cryptoHolding.getAmount().subtract(amount));
            wallet.setBalance(wallet.getBalance().add(totalCost));
            cryptoHoldingRepository.save(cryptoHolding);
        } else {
            throw new IllegalArgumentException("Invalid trade type");
        }

        walletRepository.save(wallet);

        Trade trade = new Trade();
        trade.setUser(user);
        trade.setTradeType(TradeType.valueOf(tradeType.toUpperCase()));
        trade.setCurrencyPair(currencyPair);
        trade.setPrice(price);
        trade.setAmount(amount);
        trade.setCreatedAt(LocalDateTime.now());
        Trade savedTrade = tradeRepository.save(trade);

        TradingResponse tradingResponse = new TradingResponse();
        tradingResponse.setTradeId(savedTrade.getId());
        tradingResponse.setTradeType(savedTrade.getTradeType());
        tradingResponse.setCurrencyPair(savedTrade.getCurrencyPair());
        tradingResponse.setPrice(savedTrade.getPrice());
        tradingResponse.setAmount(savedTrade.getAmount());
        tradingResponse.setTotal(totalCost);

        return tradingResponse;
    }

    @Override
    public List<TradeDTO> getUserTradingHistory(Long userId) {
        List<Trade> tradeHistory = tradeRepository.findByUserOrderByCreatedAtDesc(userId);
        return tradeHistory.stream().map(trade -> {
            TradeDTO tradeDTO = new TradeDTO();
            tradeDTO.setId(trade.getId());
            tradeDTO.setType(trade.getTradeType().toString());
            tradeDTO.setCurrencyPair(trade.getCurrencyPair());
            tradeDTO.setPrice(trade.getPrice());
            tradeDTO.setAmount(trade.getAmount());
            tradeDTO.setCreatedAt(trade.getCreatedAt());
            return tradeDTO;
        }).toList();
    }
}
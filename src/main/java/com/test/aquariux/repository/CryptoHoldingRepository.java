package com.test.aquariux.repository;

import com.test.aquariux.entity.CryptoHolding;
import com.test.aquariux.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoHoldingRepository extends JpaRepository<CryptoHolding, Long> {
    CryptoHolding findByWalletAndCryptoCurrency(Wallet wallet, String cryptoCurrency);
    List<CryptoHolding> findByWallet(Wallet wallet);
}
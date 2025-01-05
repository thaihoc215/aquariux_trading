package com.test.aquariux.repository;

import com.test.aquariux.entity.Wallet;
import com.test.aquariux.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserAndCurrency(User user, String currency);
    Wallet findByUserId(Long userId);
}
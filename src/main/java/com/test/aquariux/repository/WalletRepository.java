package com.test.aquariux.repository;

import com.test.aquariux.entity.Wallet;
import com.test.aquariux.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(Long userId);
}
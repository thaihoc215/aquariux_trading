package com.test.aquariux.repository;

import com.test.aquariux.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("SELECT t FROM Trade t WHERE t.user.id = :userId ORDER BY t.createdAt DESC")
    List<Trade> findByUserOrderByCreatedAtDesc(@Param("userId") Long userId);
}
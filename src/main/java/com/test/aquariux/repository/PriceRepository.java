package com.test.aquariux.repository;

import com.test.aquariux.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {

    Price findTopByCurrencyPairOrderByCreatedAtDesc(String currencyPair);

}

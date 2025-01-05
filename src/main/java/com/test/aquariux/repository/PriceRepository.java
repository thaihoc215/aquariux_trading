package com.test.aquariux.repository;

import com.test.aquariux.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findTopByCurrencyPairOrderByCreatedAtDesc(String currencyPair);

}

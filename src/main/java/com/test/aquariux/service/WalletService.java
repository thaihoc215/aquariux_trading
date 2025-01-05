package com.test.aquariux.service;

import com.test.aquariux.dto.UserWalletDto;
import com.test.aquariux.dto.WalletDto;

public interface WalletService {
    UserWalletDto getUserWallets(Long userId);

    WalletDto findByUserId(Long userId);
}
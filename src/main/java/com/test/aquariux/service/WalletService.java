package com.test.aquariux.service;

import com.test.aquariux.dto.UserWalletDto;

public interface WalletService {
    UserWalletDto getUserWallets(Long userId);
}
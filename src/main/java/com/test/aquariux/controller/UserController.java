package com.test.aquariux.controller;

import com.test.aquariux.dto.UserWalletDto;
import com.test.aquariux.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final WalletService walletService;

    public UserController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{userId}/wallet")
    public UserWalletDto getUserWallets(@PathVariable Long userId) {
        return walletService.getUserWallets(userId);
    }
}

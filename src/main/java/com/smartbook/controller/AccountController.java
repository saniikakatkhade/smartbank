package com.smartbook.controller;

import com.smartbook.dto.AccountResponse;
import com.smartbook.dto.DepositRequest;
import com.smartbook.dto.TransferRequest;
import com.smartbook.dto.WithdrawRequest;
import com.smartbook.entity.Account;
import com.smartbook.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            Authentication authentication) {

        Account account =
                accountService.createAccount(authentication.getName());

        AccountResponse response =
                new AccountResponse(
                        account.getId(),
                        account.getUser().getEmail(),
                        account.getBalance()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getMyAccount(
            Authentication authentication) {

        Account account =
                accountService.getMyAccount(authentication.getName());

        AccountResponse response =
                new AccountResponse(
                        account.getId(),
                        account.getUser().getEmail(),
                        account.getBalance()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponse> deposit(
            Authentication authentication,
            @Valid @RequestBody DepositRequest request) {

        Account account =
                accountService.deposit(
                        authentication.getName(),
                        request.getAmount()
                );

        AccountResponse response =
                new AccountResponse(
                        account.getId(),
                        account.getUser().getEmail(),
                        account.getBalance()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            Authentication authentication,
            @Valid @RequestBody WithdrawRequest request) {

        Account account =
                accountService.withdraw(
                        authentication.getName(),
                        request.getAmount()
                );

        AccountResponse response =
                new AccountResponse(
                        account.getId(),
                        account.getUser().getEmail(),
                        account.getBalance()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            Authentication authentication,
            @Valid @RequestBody TransferRequest request) {

        accountService.transfer(
                authentication.getName(),
                request.getRecipientEmail(),
                request.getAmount()
        );

        return ResponseEntity.ok("Transfer successful");
    }
}
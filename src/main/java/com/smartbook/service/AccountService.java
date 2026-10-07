package com.smartbook.service;

import com.smartbook.entity.Account;
import com.smartbook.entity.User;
import com.smartbook.fraud.FraudDetectionService;
import com.smartbook.fraud.FraudResult;
import com.smartbook.repository.AccountRepository;
import com.smartbook.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final FraudDetectionService fraudDetectionService;
    private final FraudAlertService fraudAlertService;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository,
            TransactionService transactionService,
            FraudDetectionService fraudDetectionService,
            FraudAlertService fraudAlertService) {

        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.fraudDetectionService = fraudDetectionService;
        this.fraudAlertService = fraudAlertService;
    }

    public Account createAccount(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (accountRepository.existsByUser(user)) {
            throw new RuntimeException("Account already exists");
        }

        Account account = new Account(
                user,
                BigDecimal.ZERO
        );

        return accountRepository.save(account);
    }

    public Account getMyAccount(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return accountRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));
    }

    public Account deposit(
            String email,
            BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Deposit amount must be greater than zero"
            );
        }

        Account account = getMyAccount(email);

        account.setBalance(
                account.getBalance().add(amount)
        );

        Account savedAccount =
                accountRepository.save(account);

        transactionService.recordTransaction(
                "DEPOSIT",
                amount,
                email,
                null,
                "SUCCESS",
                account.getId(),
                null
        );

        return savedAccount;
    }

    public Account withdraw(
            String email,
            BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        Account account = getMyAccount(email);

        if (account.getBalance().compareTo(amount) < 0) {

            throw new RuntimeException(
                    "Insufficient balance"
            );
        }

        account.setBalance(
                account.getBalance().subtract(amount)
        );

        Account savedAccount =
                accountRepository.save(account);

        transactionService.recordTransaction(
                "WITHDRAW",
                amount,
                email,
                null,
                "SUCCESS",
                account.getId(),
                null
        );

        return savedAccount;
    }

    @Transactional
    public void transfer(
            String senderEmail,
            String recipientEmail,
            BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Transfer amount must be greater than zero"
            );
        }

        if (senderEmail.equalsIgnoreCase(recipientEmail)) {

            throw new RuntimeException(
                    "Cannot transfer money to yourself"
            );
        }

        Account senderAccount =
                getMyAccount(senderEmail);

        User recipient =
                userRepository.findByEmail(recipientEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recipient not found"
                                ));

        Account recipientAccount =
                accountRepository.findByUser(recipient)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recipient account not found"
                                ));

        if (senderAccount.getBalance()
                .compareTo(amount) < 0) {

            throw new RuntimeException(
                    "Insufficient balance"
            );
        }

        // Fraud detection
        FraudResult fraudResult =
                fraudDetectionService.checkTransaction(
                        senderEmail,
                        recipientEmail,
                        amount
                );

        // Fraud detected
        if (fraudResult.isFraud()) {

            // Save fraud alert
            fraudAlertService.createAlert(
                    null,
                    senderAccount.getId(),
                    amount,
                    fraudResult.getRiskScore(),
                    fraudResult.getRiskLevel(),
                    fraudResult.getReason()
            );

            // Block transaction
            throw new RuntimeException(
                    "Transaction blocked due to suspected fraud. "
                            + "Risk Score: "
                            + fraudResult.getRiskScore()
                            + ", Risk Level: "
                            + fraudResult.getRiskLevel()
                            + ", Reason: "
                            + fraudResult.getReason()
            );
        }

        // Transfer money
        senderAccount.setBalance(
                senderAccount.getBalance()
                        .subtract(amount)
        );

        recipientAccount.setBalance(
                recipientAccount.getBalance()
                        .add(amount)
        );

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        // Record successful transaction
        transactionService.recordTransaction(
                "TRANSFER",
                amount,
                senderEmail,
                recipientEmail,
                "SUCCESS",
                senderAccount.getId(),
                recipientAccount.getId()
        );
    }
}
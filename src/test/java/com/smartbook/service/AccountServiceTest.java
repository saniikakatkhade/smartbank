package com.smartbook.service;

import com.smartbook.entity.Account;
import com.smartbook.entity.User;
import com.smartbook.fraud.FraudDetectionService;
import com.smartbook.fraud.FraudResult;
import com.smartbook.repository.AccountRepository;
import com.smartbook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private FraudDetectionService fraudDetectionService;

    @Mock
    private FraudResult fraudResult;

    @InjectMocks
    private AccountService accountService;

    private User user;
    private Account account;

    @BeforeEach
    void setUp() {

        user = new User();

        account = new Account(
                user,
                new BigDecimal("1000.00")
        );
    }

    @Test
    void depositShouldIncreaseBalance() {

        when(userRepository.findByEmail("test123@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.deposit(
                "test123@gmail.com",
                new BigDecimal("500.00")
        );

        assertEquals(
                new BigDecimal("1500.00"),
                result.getBalance()
        );

        verify(accountRepository).save(account);

        verify(transactionService).recordTransaction(
                eq("DEPOSIT"),
                eq(new BigDecimal("500.00")),
                eq("test123@gmail.com"),
                isNull(),
                eq("SUCCESS"),
                isNull(),
                isNull()
        );
    }

    @Test
    void withdrawShouldDecreaseBalance() {

        when(userRepository.findByEmail("test123@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.withdraw(
                "test123@gmail.com",
                new BigDecimal("300.00")
        );

        assertEquals(
                new BigDecimal("700.00"),
                result.getBalance()
        );

        verify(accountRepository).save(account);

        verify(transactionService).recordTransaction(
                eq("WITHDRAW"),
                eq(new BigDecimal("300.00")),
                eq("test123@gmail.com"),
                isNull(),
                eq("SUCCESS"),
                isNull(),
                isNull()
        );
    }

    @Test
    void withdrawShouldThrowExceptionWhenBalanceIsInsufficient() {

        when(userRepository.findByEmail("test123@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(Optional.of(account));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.withdraw(
                        "test123@gmail.com",
                        new BigDecimal("2000.00")
                )
        );

        assertEquals(
                "Insufficient balance",
                exception.getMessage()
        );

        verify(accountRepository, never())
                .save(any(Account.class));

        verify(transactionService, never())
                .recordTransaction(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                );
    }

    @Test
    void depositShouldRejectNegativeAmount() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.deposit(
                        "test123@gmail.com",
                        new BigDecimal("-100.00")
                )
        );

        assertEquals(
                "Deposit amount must be greater than zero",
                exception.getMessage()
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }

    @Test
    void withdrawShouldRejectZeroAmount() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.withdraw(
                        "test123@gmail.com",
                        BigDecimal.ZERO
                )
        );

        assertEquals(
                "Withdrawal amount must be greater than zero",
                exception.getMessage()
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }

    @Test
    void transferShouldMoveMoneyBetweenAccounts() {

        User recipientUser = new User();

        Account recipientAccount = new Account(
                recipientUser,
                new BigDecimal("500.00")
        );

        when(userRepository.findByEmail("test123@gmail.com"))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmail("receiver@gmail.com"))
                .thenReturn(Optional.of(recipientUser));

        when(accountRepository.findByUser(user))
                .thenReturn(Optional.of(account));

        when(accountRepository.findByUser(recipientUser))
                .thenReturn(Optional.of(recipientAccount));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(fraudDetectionService.checkTransaction(
                anyString(),
                anyString(),
                any(BigDecimal.class)
        )).thenReturn(fraudResult);

        when(fraudResult.isFraud())
                .thenReturn(false);

        accountService.transfer(
                "test123@gmail.com",
                "receiver@gmail.com",
                new BigDecimal("200.00")
        );

        assertEquals(
                new BigDecimal("800.00"),
                account.getBalance()
        );

        assertEquals(
                new BigDecimal("700.00"),
                recipientAccount.getBalance()
        );

        verify(accountRepository, times(2))
                .save(any(Account.class));
    }
}
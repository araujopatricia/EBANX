package com.ebanx.techtest.service;

import com.ebanx.techtest.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {
    private Map<String, Account> accounts = new HashMap<>();

    public void reset() {
        accounts.clear();
    }

    public Optional<Account> getAccount(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Account deposit(String accountId, int amount) {
        Account account = accounts.get(accountId);
        if (account == null) {
            account = new Account(accountId, amount);
            accounts.put(accountId, account);
        } else {
            account.deposit(amount);
        }
        return account;
    }

    public Optional<Account> withdraw(String accountId, int amount) {
        Account account = accounts.get(accountId);
        if (account != null) {
            account.withdraw(amount);
        }
        return Optional.ofNullable(account);
    }

    public Optional<Map<String, Account>> transfer(String originId, String destinationId, int amount) {
        Account origin = accounts.get(originId);
        if (origin == null) {
            return Optional.empty();
        }

        origin.withdraw(amount);

        Account destination = accounts.get(destinationId);
        if (destination == null) {
            destination = new Account(destinationId, amount);
            accounts.put(destinationId, destination);
        } else {
            destination.deposit(amount);
        }

        Map<String, Account> result = new HashMap<>();
        result.put("origin", origin);
        result.put("destination", destination);
        return Optional.of(result);
    }
}

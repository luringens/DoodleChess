package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests concerning the behavior of the AccountManager class.
 */
class accountManagerTest {

    @Test
    void newAccountManagerHasNoAccounts() {
        AccountManager am = new AccountManager();
        assertEquals(0, am.accountSize());
    }

    @Test
    void accountManagerCanRetrieveAccountByName() {
        AccountManager am = new AccountManager();
        Account a = new Account("Alice");
        Account b = new Account("Bob");
        
        am.addAccount(a);
        am.addAccount(b);
        
        assertEquals(a, am.getAccount("Alice"));
        assertEquals(b, am.getAccount("Bob"));
    }

    @Test
    void accountManagerDoesNotAddAccountWithSameNameTwice() {
        AccountManager am = new AccountManager();
        Account a1 = new Account("Alice");
        Account a2 = new Account("Alice");

        am.addAccount(a1);
        am.addAccount(a2);

        assertEquals(1, am.accountSize());
    }

    @Test
    void savedAccountStoresCorrectData() {
        AccountManager am = new AccountManager();
        Account a1 = new Account("Alice", 10, 20, 1250);
        assertEquals("Alice", a1.getName());
        assertEquals(10, a1.getWinCount());
        assertEquals(20, a1.getLossCount());
        assertEquals(1250, a1.getRating());

        am.addAccount(a1);

        am.save("test.db");
        am.load("test.db");

        Account a2 = am.getAccount(a1.getName());
        assertEquals(a1.getName(), a2.getName());
        assertEquals(a1.getWinCount(), a2.getWinCount());
        assertEquals(a1.getLossCount(), a2.getLossCount());
        assertEquals(a1.getRating(), a2.getRating());
    }

}

package com.syntax_highlighters.chess.general;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import com.syntax_highlighters.chess.account.Account;
import com.syntax_highlighters.chess.account.AccountManager;

import org.junit.jupiter.api.Test;

/**
 * Tests concerning the behavior of the AccountManager class.
 */
class accountManagerTest {

    @Test
    void newAccountManagerHasNoAccounts() {
        String file = "newAccountManagerHasNoAccounts.db";
        new File(file).deleteOnExit();
        AccountManager am = new AccountManager(file);
        assertEquals(0, am.accountSize());
    }

    @Test
    void accountManagerCanRetrieveAccountByName() {
        String file = "accountManagerCanRetrieveAccountByName.db";
        new File(file).deleteOnExit();
        AccountManager am = new AccountManager(file);
        Account a = new Account("Alice");
        Account b = new Account("Bob");
        
        am.addAccount(a);
        am.addAccount(b);
        
        assertEquals(a, am.getAccount("Alice"));
        assertEquals(b, am.getAccount("Bob"));
    }

    @Test
    void accountManagerDoesNotAddAccountWithSameNameTwice() {
        String file = "accountManagerDoesNotAddAccountWithSameNameTwice.db";
        new File(file).deleteOnExit();
        AccountManager am = new AccountManager(file);
        Account a1 = new Account("Alice");
        Account a2 = new Account("Alice");

        am.addAccount(a1);
        am.addAccount(a2);

        assertEquals(1, am.accountSize());
    }

    @Test
    void savedAccountStoresCorrectData() {
        String file = "savedAccountStoresCorrectData.db";
        new File(file).deleteOnExit();
        String name = "Alice";
        int wins = 10;
        int losses = 20;
        {
            AccountManager am = new AccountManager(file);
            Account ac = new Account(name, wins, losses);
            assertEquals(name, ac.getName());
            assertEquals(wins, ac.getWinCount());
            assertEquals(losses, ac.getLossCount());
            am.addAccount(ac);
            am.save();
        }
        {
            AccountManager am = new AccountManager(file);
            Account ac = am.getAccount(name);
            assertEquals(name, ac.getName());
            assertEquals(wins, ac.getWinCount());
            assertEquals(losses, ac.getLossCount());
        }
    }

    @Test
    void accountManagerUpdatesScoreCorrectlyForSamePlayerWinningTwice() {
        String file = "accountManagerUpdatesScoreCorrectlyForSamePlayerWinningTwice.db";
        new File(file).deleteOnExit();
        AccountManager am = new AccountManager(file);
        Account a = new Account("Alice"); // initial rating: 1000
        Account b = new Account("Bob");   // initial rating: 1000
        am.addAccount(a);
        am.addAccount(b);
        am.updateRating(a, b);
        // a.getRating() = (1000 + 400*1) / 1 = 1400
        // b.getRating() = (1000 - 400*1) / 1 = 600
        am.updateRating(a, b);
        // a.getRating() = ((1000 + 600) + 400*2) / 2 = 1200
        //                           ^ new rating of opponent
        // b.getRating() = ((1000 + 1400) - 400*2) / 2 = 800
        //                           ^ new rating of opponent
        assertEquals(1200, a.getRating());
        assertEquals(800, b.getRating());
        // This is based on the formula from Wikipedia:
        // rating = (<sum of opponent ratings> + 400*(wins - losses)) / <games played>
        // https://en.wikipedia.org/wiki/Elo_rating_system#Performance_rating
    }
}

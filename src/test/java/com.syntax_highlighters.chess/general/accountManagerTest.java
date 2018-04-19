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

    @Test
    void accountManagerUpdatesScoreCorrectlyForSamePlayerWinningTwice() {
        AccountManager am = new AccountManager();
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

    // TODO: add test to ensure draws don't affect the score wrong
    // TODO: add tests to ensure unrated wins/losses don't affect the score wrong
}

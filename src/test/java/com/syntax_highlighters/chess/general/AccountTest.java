package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Account;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void defaultAccountCreationHasNoWinsOrLosses() {
        Account a = new Account("Alice");
        assertEquals(0, a.getWinCount());
        assertEquals(0, a.getLossCount());
    }

    @Test
    void winIncrementsWinCountByOne() {
        Account a = new Account("Alice");
        int wc = a.getWinCount();

        a.win(1000);

        assertEquals(wc+1, a.getWinCount());
    }

    @Test
    void winDoesNotChangeLossCount() {
        Account a = new Account("Alice");
        int lc = a.getLossCount();

        a.win(1000);

        assertEquals(lc, a.getLossCount());
    }
    
    @Test
    void lossIncrementsLossCountByOne() {
        Account a = new Account("Alice");
        int lc = a.getLossCount();

        a.loss(1000);

        assertEquals(lc+1, a.getLossCount());
    }

    @Test
    void lossDoesNotChangeWinCount() {
        Account a = new Account("Alice");
        int wc = a.getWinCount();

        a.loss(1000);

        assertEquals(wc, a.getWinCount());
    }

    @Test
    void accountStoresCorrectName() {
        String name = "Alice";
        Account a = new Account(name);
        assertEquals(name, a.getName());
    }

    @Test
    void defaultRatingOfAccountIs1000() {
        Account a = new Account("Alice");
        assertEquals(1000, a.getRating());
    }
}

package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    @Test
    public void defaultAccountCreationHasNoWinsOrLosses() {
        Account a = new Account("Alice");
        assertEquals(0, a.getWinCount());
        assertEquals(0, a.getLossCount());
    }

    @Test
    public void winIncrementsWinCountByOne() {
        Account a = new Account("Alice");
        int wc = a.getWinCount();

        a.win();

        assertEquals(wc+1, a.getWinCount());
    }

    @Test
    public void winDoesNotChangeLossCount() {
        Account a = new Account("Alice");
        int lc = a.getLossCount();

        a.win();

        assertEquals(lc, a.getLossCount());
    }
    
    @Test
    public void lossIncrementsLossCountByOne() {
        Account a = new Account("Alice");
        int lc = a.getLossCount();

        a.loss();

        assertEquals(lc+1, a.getLossCount());
    }

    @Test
    public void lossDoesNotChangeWinCount() {
        Account a = new Account("Alice");
        int wc = a.getWinCount();

        a.loss();

        assertEquals(wc, a.getWinCount());
    }

    @Test
    public void accountStoresCorrectName() {
        String name = "Alice";
        Account a = new Account(name);
        assertEquals(name, a.getName());
    }

    @Test
    public void defaultRatingOfAccountIs1000() {
        Account a = new Account("Alice");
        assertEquals(1000, a.getRating());
    }

    // TODO: Tests that the rating updates correctly, once the new API is
    // hammered out
}

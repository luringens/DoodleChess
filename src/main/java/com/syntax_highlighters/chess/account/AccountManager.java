package com.syntax_highlighters.chess.account;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class containing a list of accounts, and the ability to perform operations on
 * them, such as fetching a given account or return a sorted list of accounts.
 */
public class AccountManager {
    private List<Account> myAccounts = new ArrayList<>();
    private final String filename;

    public AccountManager(String filename) {
        this.filename = filename;

        // Hack to register the SQLite class
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Failed to create database.");
        }

        if (!Files.exists(Paths.get(filename))) {
            createDatabase();
            createTable();
        }
        else load();
    }

    public int accountSize () {
        return myAccounts.size();
    }

    /**
     * Get the account with the given name.
     *
     * @param name The account name
     * @return The account whose name equals the parameter, or null if no such
     * account exists
     */

    public Account getAccount (String name) {
        for (Account a : myAccounts)
            if (a.getName().equals(name))
                return a;
        return null;
    }

    /**
     * Helper method: return a list of accounts sorted by their win counts.
     * <p>
     * SUGGESTION: Make use of Comparator object which from the beginning
     * sorts list in reversed order.
     *
     * @param accounts The list of accounts to sort
     * @return a properly sorted list of accounts
     */
    private List<Account> sort(List<Account> accounts) {
        accounts.sort(Comparator.comparing(Account::getWinCount));
        List<Account> reverseAccounts = new ArrayList<>();
        for (int i = accounts.size() - 1; i >= 0; i--)
            reverseAccounts.add(accounts.get(i));
        return reverseAccounts;
    }

    /**
     * Add a new account if it does not already exist.
     *
     * @param acc The account to add
     * @return true if the account was added, false if it already existed in the
     * account list
     */
    public boolean addAccount(Account acc) {
        boolean canAdd = true;
        if (myAccounts.isEmpty())
            canAdd = true;
        else {
            for (Account a : myAccounts)
                if (a.getName().equals(acc.getName()))
                    canAdd = false;
        }
        if (canAdd)
            myAccounts.add(acc);
        save();
        return canAdd;
    }

    /**
     * Return the top n accounts ordered by win count.
     *
     * @param n Number of accounts to return
     * @return A correctly ordered list of n accounts
     */
    public List<Account> getTop (int n) {
        myAccounts = sort(myAccounts);
        if (myAccounts.size() <= n)
            return (myAccounts);
        List<Account> returnAccounts = new ArrayList<>();
        for (int i = 0; i < n; i++)
            returnAccounts.add(myAccounts.get(i));
        return returnAccounts;
    }

    /**
     * Return all the accounts ordered by win count.
     *
     * @return A correctly ordered list containing all the accounts
     */
    public List<Account> getAll() {
        myAccounts = sort(myAccounts);
        return myAccounts;
    }

    /**
     * Save the account list to disk.
     * This is done automatically in all accountmanager functions that edit
     * the account list.
     * <p>
     * Overwrites the file in question, or creates it if it doesn't already
     * exist.
     * <p>
     * SUGGESTION: Return boolean value indicating whether or not saving the
     * file succeeded.
     */
    public void save() {
        try {
            Connection conn = connect();

            // Horrible, I know.
            // Sorry.
            Statement deleteAll = conn.createStatement();
            deleteAll.executeUpdate("DELETE FROM person");

            for (Account a : myAccounts) {
                PreparedStatement stmt = conn.prepareStatement(a.insertStatement());
                stmt.setString(1, a.getName());
                stmt.setInt(2, a.getRating());
                stmt.setInt(3, a.getWinCount());
                stmt.setInt(4, a.getLossCount());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Connection connect() throws SQLException {
        // SQLite connection string
        String pathh = Paths.get(filename).toAbsolutePath().toString();
        String url = "jdbc:sqlite:" + pathh;
        return DriverManager.getConnection(url);
    }

    private void createDatabase() {
        String pathh = Paths.get(filename).toAbsolutePath().toString();
        String url = "jdbc:sqlite:" + pathh;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a conenction to the new db");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void createTable() {
        String pathh = Paths.get(filename).toAbsolutePath().toString();
        String url = "jdbc:sqlite:" + pathh;

        String sql = "CREATE TABLE person (\n"
                + "	name text PRIMARY KEY,\n"
                + "	score integer ,\n"
                + "	wins integer,\n"
                + "	losses integer\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Load the accounts from a file with the given filename into this
     * AccountManager.
     * <p>
     * SUGGESTION: Return boolean value indicating whether or not loading the
     * file succeeded.
     */
    private void load () {
        String sql = "SELECT * FROM person";

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            myAccounts.clear(); // ensure there are no accounts in the list before loading

            // loop through the result set
            while (rs.next()) {
                int score = rs.getInt("score");
                String name = rs.getString("name");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                myAccounts.add(new Account(name, wins, losses, score));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Updates the rating for two accountswith the results of a game.
     * @param winner The winner's account.
     * @param loser The losers. account.
     */
    public void updateRating(Account winner, Account loser){
        int loserRating = loser.getRating();
        int winnerRating = winner.getRating();
        winner.win(loserRating);
        loser.loss(winnerRating);
        save();
    }

    /**
     * Updates the rating for two accountswith the results of a game.
     * @param acc1 The first account.
     * @param acc2 The second account.
     */
    public void updateRatingDraw(Account acc1, Account acc2){
        // This function does nothing at the moment,
        // but is reserved for possible future use.
    }
}

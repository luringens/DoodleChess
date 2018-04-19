package com.syntax_highlighters.chess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class containing a list of accounts, and the ability to perform operations on
 * them, such as fetching a given account or return a sorted list of accounts.
 */
public class AccountManager {
    List<Account> myAccounts = new ArrayList<>();

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
    private List<Account> sort (List<Account> accounts) {
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
    public boolean addAccount (Account acc) {
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
    public List<Account> getAll () {
        myAccounts = sort(myAccounts);
        return myAccounts;
    }

    /**
     * Save the account list to a file with the given filename.
     * <p>
     * Overwrites the file in question, or creates it if it doesn't already
     * exist.
     * <p>
     * SUGGESTION: Return boolean value indicating whether or not saving the
     * file succeeded.
     *
     * @param filename The name of the file to save to
     */
    public void save (String filename) {
        try {
            Connection conn = connect(filename);

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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Connection connect (String filename) throws SQLException {
        // NOTE: for some reason it worked after I added this line. I don't
        // really know why. Maybe it just ensures that it has the JDBC
        // dependency loaded or something.

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // SQLite connection string
        String pathh = new File("UserAccounts.db").getAbsolutePath();
        String url = "jdbc:sqlite:" + pathh;
        return DriverManager.getConnection(url);
    }

    public static void createDatabase (String fileName) {
       String pathh = new File(fileName).getAbsolutePath();
        String url = "jdbc:sqlite:" + pathh;


        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
               // System.out.println("The driver name is " + meta.getDriverName());
                //System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTable (String filename) {
        String pathh = new File("UserAccounts.db").getAbsolutePath();
        String url = "jdbc:sqlite:" + pathh;

        String sql = "CREATE TABLE IF NOT EXISTS person (\n"
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
            System.out.println(e.getMessage());
        }
    }

    /**
     * Load the accounts from a file with the given filename into this
     * AccountManager.
     * <p>
     * SUGGESTION: Return boolean value indicating whether or not loading the
     * file succeeded.
     *
     * @param filename The name of the file to load from
     */
    public void load (String filename) {
        String sql = "SELECT * FROM person";

        try {
            Connection conn = this.connect(filename);
            Statement stmt  = conn.createStatement();
            String pathh = new File(filename).getAbsolutePath();
            try{createDatabase(filename); createTable(filename);}
            catch(Exception e){}

            ResultSet rs    = stmt.executeQuery(sql);

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
            System.out.println(e);
        }


    }

    public void updateRating(Account winner, Account loser){
        winner.addPoints(loser.getRating()+400);
        loser.addPoints(winner.getRating()+400);
        int winnerRating = winner.getPoints() / (winner.getLossCount() + winner.getWinCount()+1);
        int loserRating = loser.getPoints() / (loser.getLossCount() + loser.getWinCount()+1);
        winner.setRating(winnerRating);
        loser.setRating(loserRating);
        return;
    }

    public void updateRatingDraw(Account winner, Account loser){
        winner.addPoints(loser.getRating());
        loser.addPoints(winner.getRating());
        int winnerRating = winner.getPoints() / (winner.getLossCount() + winner.getWinCount()+1);
        int loserRating = loser.getPoints() / (loser.getLossCount() + loser.getWinCount()+1);
        winner.setRating(winnerRating);
        loser.setRating(loserRating);
        return;
    }
}

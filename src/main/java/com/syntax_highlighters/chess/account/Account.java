package com.syntax_highlighters.chess.account;

/**
 * Class containing information about a player account.
 *
 * Stores: name of account, number of wins, number of losses.
 */
public class Account {
    private final String name;
    private int wins;
    private int losses;
    private int opponentRatingSum;

    /**
     * Construct a new account with the given name.
     *
     * @param name The account username
     */
    public Account(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.opponentRatingSum = 0;
    }

    /**
     * Construct a new account with a given name and the given win/loss numbers.
     *
     * @param name The account username
     * @param wins The number of wins
     * @param losses The number of losses
     */
    public Account(String name, int wins, int losses){
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.opponentRatingSum = 0;
    }
    /**
     * Construct a new account with a given name and the given win/loss numbers.
     *
     * @param name The account username
     * @param wins The number of wins
     * @param losses The number of losses
     * @param opponentRatingSum The sum of the rating of the players opponents.
     */
    public Account(String name, int wins, int losses, int opponentRatingSum){
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.opponentRatingSum = 0;
    }


    /**
     * Get the name of the account.
     *
     * @return The name of the account
     */
    public String getName(){
        return this.name;
    }

    /**
     * Increase win count by one.
     */
    public void win(int opponentRating) {
        this.wins++;
        opponentRatingSum += opponentRating;
    }

    /**
     * Increase loss count by one.
     */
    public void loss(int opponentRating) {
        this.losses++;
        opponentRatingSum += opponentRating;
    }

    /**
     * Get the number of times this player has won.
     *
     * @return The win count for this account
     */
    public int getWinCount(){
        return this.wins;
    }

    /**
     * Get the number of times this player has lost.
     *
     * @return The loss count for this account
     */
    public int getLossCount(){
        return this.losses;
    }

    /**
     * Get the rating of the player.
     *
     * @return The rating for this account
     */
    public int getRating() {
        return wins + losses == 0 ? 1000 : (opponentRatingSum + 400 * (wins - losses)) / (wins + losses);
    }

    /**
     * Get the win rate of this account.
     *
     * @return The percentage of games this player has won, as a number between
     * 0 and 1.
     */
    public double getScore(){
        return (wins / (double)(wins+losses));
    }

    /**
     * Helper method: Get SQL insert statement for an account.
     *
     * NOTE: Shouldn't really exist.
     *
     * @return An SQL statement as a string, usable in a prepared statement for
     * inserting an account in the database
     */
    public String insertStatement() {
        return "INSERT INTO person(name, score, wins, losses) VALUES (?, ?, ?, ?)";
    }
}

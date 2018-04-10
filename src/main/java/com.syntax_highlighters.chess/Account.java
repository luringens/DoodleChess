package com.syntax_highlighters.chess;

/**
 * Class containing information about a player account.
 *
 * Stores: name of account, number of wins, number of losses.
 */
public class Account {
    private final String name;
    private int wins;
    private int losses;
    private int rating;
    private int points;

    /**
     * Construct a new account with the given name.
     *
     * @param name The account username
     */
    public Account(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.rating = 1000;
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
        this.rating = 1000;
    }
    /**
     * Construct a new account with a given name and the given win/loss numbers.
     *
     * @param name The account username
     * @param wins The number of wins
     * @param losses The number of losses
     * @param rating The rating of player
     */
    public Account(String name, int wins, int losses, int rating){
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.rating = rating;
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
    public void win(){
        this.wins++;
    }

    /**
     * Increase loss count by one.
     */
    public void loss(){
        this.losses++;
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
        return this.rating;
    }

    /**
     * Get the points of the player.
     *
     * @return The points for this account
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Set the points of the player.
     *
     * @return void
     */
    public void addPoints(int n) {
        this.points += n;
    }

    /**
     * Set the rating of the player.
     *
     * @return void
     */
    public void setRating(int rating) {
        this.rating = rating;
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
}

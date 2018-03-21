package com.syntax_highlighters.chess;

public class Account {
    private String name;
    private int wins;
    private int losses;

    /**
     * Construct a new account with the given name.
     *
     * @param name The account username
     */
    public Account(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
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
    }

    public String getName(){
        return this.name;
    }

    public void win(){
        this.wins++;
    }

    public void loss(){
        this.losses++;
    }

    public int getWinCount(){
        return this.wins;
    }

    public int getLossCount(){
        return this.losses;
    }

    public double getScore(){
        return (wins / (double)(wins+losses));
    }
}

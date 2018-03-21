package com.syntax_highlighters.chess;

public class Account {
    private String name;
    private int wins;
    private int losses;

    /**
     * Constructor.
     *
     * Account, YEY :)
     */
    public Account(String name) {
        this.name = name;
    }

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
        return (wins / (wins+losses));
    }
}

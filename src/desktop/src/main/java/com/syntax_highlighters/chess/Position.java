package com.syntax_highlighters.chess;

/**
 * Position on a grid (x, y).
 *
 * This class should be immutable.
 */
public class Position {

    private int x, y;
    
    /**
     * Constructor.
     *
     * IDEA: Check that the position is within bounds. May cause problems later
     * if we're going to create a game with different rules, where the range of
     * positions might be different.
     *
     * @param x The X coordinate on the board
     * @param y The Y coordinate on the board
     */
    public Position(int x, int y) {
        this.x = x; this.y = y;
    }


    /**
     * Get the X coordinate of this Position.
     *
     * @return The X coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the X coordinate of this Position.
     *
     * @return The X coordinate.
     */
    public int getY() {
        return this.y;
    }
}

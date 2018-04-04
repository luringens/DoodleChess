package com.syntax_highlighters.chess;

import java.util.Objects; // Objects.hash
import java.util.Arrays; // asList
import java.util.List;

/**
 * Position on a grid (x, y).
 *
 * This class should be immutable. The Position is one-based.
 */
public class Position {

    private final int x;
    private final int y;
    
    /**
     * Constructor.
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
     * Get the Y coordinate of this Position.
     *
     * @return The Y coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * The position n steps up on the board.
     *
     * @return A new position n steps above this position
     */
    public Position north(int nSteps) {
        return new Position(this.x, this.y + nSteps);
    }

    /**
     * The position n steps down on the board.
     *
     * @return A new position n steps below this position
     */
    public Position south(int nSteps) {
        return new Position(this.x, this.y - nSteps);
    }

    /**
     * The position n steps left on the board.
     *
     * @return A new position n steps to the left of this position
     */
    public Position west(int nSteps) {
        return new Position(this.x - nSteps, this.y);
    }
    
    /**
     * The position n steps right on the board.
     *
     * @return A new position n steps to the right of this position
     */
    public Position east(int nSteps) {
        return new Position(this.x + nSteps, this.y);
    }

    /**
     * The position n steps diagonally up and left on the board.
     *
     * @return A new position n steps above and to the left of this position
     */
    public Position northwest(int nSteps) {
        return new Position(this.x - nSteps, this.y + nSteps);
    }
    
    /**
     * The position n steps diagonally up and right on the board.
     *
     * @return A new position n steps above and to the right of this position
     */
    public Position northeast(int nSteps) {
        return new Position(this.x + nSteps, this.y + nSteps);
    }
    
    /**
     * The position n steps down and left on the board.
     *
     * @return A new position n steps below and to the left of this position
     */
    public Position southwest(int nSteps) {
        return new Position(this.x - nSteps, this.y - nSteps);
    }
    
    /**
     * The position n steps down and right on the board.
     *
     * @return A new position n steps below and to the right of this position
     */
    public Position southeast(int nSteps) {
        return new Position(this.x + nSteps, this.y - nSteps);
    }

    /**
     * Return all the neighbors of a Position.
     *
     * A neighbor is any adjacent or diagonally adjacent Position to this
     * position. No bounds checks.
     *
     * @return A list of this postion's neighbors
     */
    public List<Position> neighbors() {
        return Arrays.asList(
                north    (1), south    (1),
                east     (1), west     (1),
                northeast(1), northwest(1),
                southeast(1), southwest(1));
    }

    /**
     * Return the position in chess notation.
     *
     * @return "(x,y)" where x is the x coordinate and y is the y coordinate
     */
    @Override
    public String toString() {
        return String.format("(%d,%d)", this.x, this.y);
    }

    /**
     * Parse a board position written in chess notation, and return a Position.
     *
     * @param pos The position in chess notation, of the form [A-Ha-h][1-8]
     * @return The corresponding logical position
     */
    public static Position fromChessNotation(String pos) {
        // precondition: pos is of the form [A-Ha-h][1-8]
        String lowerPos = pos.toLowerCase(); // ignore case
        assert lowerPos.length() == 2;
        
        char file = lowerPos.charAt(0);
        char rank = lowerPos.charAt(1);
        assert "abcdefgh".contains(""+file);
        assert "12345678".contains(""+rank);
        
        int xPos = file - 'a' + 1;
        int yPos = rank - '0';
        return new Position(xPos, yPos);
    }

    /**
     * Custom equality method.
     *
     * Two positions are equal if their x and y coordinates are equal.
     *
     * @param other The object to test for equality
     * @return true if the object is equal to this Position, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Position)) return false;
        Position o = (Position) other;
        return o.x == this.x && o.y == this.y;
    }

    /**
     * Custom hashCode method.
     *
     * Use Objects.hash for simplicity. Considers the x and y coordinate.
     *
     * @return The hash of the Position
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public Position copy() {
        return new Position(this.x, this.y);
    }
}

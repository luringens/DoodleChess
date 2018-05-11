package com.syntax_highlighters.chess;

/**
 * Represents whether a piece is black or white.
 */
public class Color implements java.io.Serializable {
    private final boolean color;

    private static boolean WHITE_B = true;
    private static boolean BLACK_B = false;

    /**
     * IMPORTANT: This must be changed on every release of the class
     * in order to prevent cross-version serialization.
     */
    private static final long serialVersionUID = 1;

    /**
     * The instance of color that is white.
     */
    public static final Color WHITE = new Color(WHITE_B);

    /**
     * The instance of color that is black.
     */
    public static final Color BLACK = new Color(BLACK_B);

    /**
     * Helper constructor: Create a new Color object.
     *
     * @param color true if the color is white, otherwise false if it is black
     */
    private Color(boolean color) {
        this.color = color;
    }

    /**
     * Whether or not the color is white.
     * @return Whether or not the color is white.
     */
    public Boolean isWhite() {
        return color == WHITE_B;
    }

    /**
     * Whether or not the color is black.
     * @return Whether or not the color is white.
     */
    public Boolean isBlack() {
        return color == BLACK_B;
    }

    /**
     * The opposite color of this.
     * @return The opposite color of this.
     */
    public Color opponentColor() {
        if (this == WHITE) return BLACK;
        else return WHITE;
    }
}

package com.syntax_highlighters.chess;

/**
 * Holds the current state of the board.
 *
 * The board state is stored in a 8x8 grid containing the pieces (null values
 * signifying empty squares).
 *
 * The white player starts at rows 0 and 1 (zero-based).
 * The black player starts at rows 6 and 7 (zero-based).
 */
public class Board {
    // constants, just in case
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;

    IChessPiece[][] pieces;
    
    /**
     * Sets up a new game with the white and black players.
     *
     * @param white The white player
     * @param black The black player
     */
    public void setupNewGame(IPlayer white, IPlayer black) {
        IChessPiece[][] pieces = new IChessPiece[BOARD_HEIGHT][BOARD_WIDTH];

        // TODO: figure out a good way to put all the pieces correctly
        // Option 1: Just specify everything
        // Option 2: Use "fromChessNotation"
        //   - What is this a method of? AbstractChessPiece?
        // Option 3: ???
    }

    /**
     * Put a piece at a position on the board.
     *
     * @param pos The position where the piece should be put
     * @param piece The piece to place
     */
    public void putAtPosition(Position pos, IChessPiece piece) {
        assert pos.getX() >= 1;
        assert pos.getY() >= 1;
        assert pos.getX() <= BOARD_WIDTH;
        assert pos.getY() <= BOARD_HEIGHT;
        
        // NOTE: There is redundancy in the position, this might cause bugs.
        pieces[pos.getY()-1][pos.getX()-1] = piece;
    }

    /**
     * Get the piece at a given position.
     * 
     * @param pos The position on the board
     * @return The piece at the position
     */

    public IChessPiece getAtPosition(Position pos) {
        assert pos.getX() >= 1;
        assert pos.getY() >= 1;
        assert pos.getX() <= BOARD_WIDTH;
        assert pos.getY() <= BOARD_HEIGHT;
        
        return pieces[pos.getY()-1][pos.getX()-1];
    }
}

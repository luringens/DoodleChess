package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.AbstractChessPiece;
import com.syntax_highlighters.chess.entities.IChessPiece;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * Holds the current state of the board.
 *
 * The board state is stored in a 8x8 grid containing the pieces (null values
 * signifying empty squares).
 *
 * The white player starts at rows 1 and 2
 * The black player starts at rows 7 and 8
 */
public class Board {
    // constants, just in case
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;

    IChessPiece[][] pieces;
    
    /**
     * Sets up a new board with the white and black players in their starting
     * positions.
     */
    public void setupNewGame() {
        IChessPiece[][] pieces = new IChessPiece[BOARD_HEIGHT][BOARD_WIDTH];

        String[] blackPieces = new String[]{
            "RA8", "NB8", "BC8", "QD8", "KE8", "BF8", "NG8", "RH8",
            "PA7", "PB7", "PC7", "PD7", "PE7", "PF7", "PG7", "PH7"
        };
        
        String[] whitePieces = new String[]{
            "PA2", "PB2", "PC2", "PD2", "PE2", "PF2", "PG2", "PH2",
            "RA1", "NB1", "BC1", "QD1", "KE1", "BF1", "NG1", "RH1"
        };

        for (String p : whitePieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, true);
            putAtPosition(piece.getPosition(), piece);
        }
        
        for (String p : blackPieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, false);
            putAtPosition(piece.getPosition(), piece);
        }
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

    // TODO JavaDoc
    public List<IChessPiece> getAllPieces() {
        throw new NotImplementedException();
    }

    // TODO JavaDoc
    public Board copy() {
        throw new NotImplementedException();
    }
}

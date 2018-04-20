package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.entities.Color;
import com.syntax_highlighters.chess.entities.IChessPiece;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that the public API of the Board class works correctly.
 *
 * @see Board
 */
class BoardTest {
    private final Board board = new Board ();
    private final Position p = new Position(3,2);
    private final IChessPiece piece = new ChessPieceKing(p, Color.WHITE);
    private final List<IChessPiece> pieceList = new ArrayList<>();


    @Test
    void putAtPositionShouldPutCorrectPieceAtCorrectPosition(){
        board.putAtPosition(p,piece);
        assertEquals(piece, board.getAtPosition(p));
    }

    //Tests that the newBoard Function return 0.
    @Test
    void emptyBoardContainsNoPieces() {
      Board b = new Board();
      assertEquals(b.getAllPieces().size(),0);

    }
    
    //Tests that the Occupied function returns true if it actually is occupied.
    @Test
    void isOccupiedReturnsTrueForOccupiedSquare(){
         Board k = new Board();
         k.putAtPosition(p,piece);
         assertTrue(k.isOccupied(p));
    }

    /*
     * Tests if the isOccupied function returns
     * false if it isn't Occupied.
     */
    @Test
    void isOccupiedReturnsFalseForEmptySquare(){
        Board k = new Board();
        assertFalse(k.isOccupied(p));
    }

    //Tests that getAtPosition on occupied square does not return null
    @Test
    void getAtPositionDoesNotReturnNullIfSquareIsOccupied(){
       Board k = new Board();
       k.putAtPosition(p,piece);
       assertNotNull(k.getAtPosition(p));
    }

    // Tests that getAtPosition on unoccupied position returns null.
    @Test
    void getAtPositionReturnsNullIfSquareIsNotOccupied(){
        Board k = new Board();
        assertNull(k.getAtPosition(p));
    }


    //Tests that the putAtEmptyPosition function works if the position is empty.
    @Test
    void putAtEmptyPositionBehavesCorrectlyWhenPositionEmpty(){
       Board k = new Board();
       k.putAtEmptyPosition(p,piece);
       assertEquals(k.getAtPosition(p),piece);
       assertNotEquals(k.getAtPosition(p),null);
    }

    /* Tests if the copy() function leaves the "real-board" unchanged,
     * and the copied board changed.
     */
    @Test
    void changesMadeToCopiedBoardDoesNotAffectOriginal(){
        Board k = new Board();
        Board h = k.copy();
        h.putAtEmptyPosition(p,piece);
        assertEquals(h.getAtPosition(p),piece);
        assertNotEquals(k.getAtPosition(p),piece);
    }
    
    /* Tests if the putAtEmptyPosition function
     * throws IllegalArgumentException if the
     * Position isn't empty.. More elegantly.
     */
    @Test
    void putAtEmptyPositionThrowsExceptionWhenPositionOccupied(){
        Board k = new Board();
        k.putAtPosition(p, piece);
       assertThrows(IllegalArgumentException.class, ()-> k.putAtEmptyPosition(p, piece));
    }
    
    /*
     *Tests if altering the list in anyway will change the state of the board.
     */
    @Test
    void getAllPiecesReturnsShallowCopyOfInternalList(){
        Board k = new Board();
        k.setupNewGame();
        k.getAllPieces().remove(5);
        assertEquals(k.getAllPieces().size(),32);

        k.getAllPieces().removeAll(k.getAllPieces());
        assertEquals(k.getAllPieces().size(),32);
    }

    @Test
    void modificationsOfDifferentBoardsAreIndependent(){

        Board k = new Board(pieceList);
        Board h = new Board();
        h.setupNewGame();
        assertNotEquals(k.getAllPieces().size(),32);
    }
}

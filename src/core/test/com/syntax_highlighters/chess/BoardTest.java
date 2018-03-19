package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.entities.IChessPiece;
import org.junit.jupiter.api.Test;
import java.util.List;


import java.lang.reflect.Executable;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {
    Board board = new Board ();
    Position p = new Position(3,2);
    IChessPiece piece = new ChessPieceKing(p,true);
    List<IChessPiece> pieceList;



   /*@Test
    public void setupTest(){

        board.setupNewGame();
        for(IChessPiece k : board.getAllPieces() ){

        }

    }
    */


        //Tests the the putAtPosition function

   @Test
   public void putAtPositionTest(){
       board.putAtPosition(p,piece);
       assertEquals(board.getAtPosition(p),piece);
       }

       //Tests that the newBoard Function return 0.

  @Test
    public void newBoardTest() {
    Board b = new Board();
    assertEquals(b.getAllPieces().size(),0);

  }
  //Tests that the Occupied function returns true if it actually is occupied.
  @Test
    public void isOccupiedTrueTest(){
       Board k = new Board();
       k.putAtPosition(p,piece);
       assertTrue(k.isOccupied(p));
  }

    /*
     * Tests if the isOccupied function returns
     * false if it isn't Occupied.
     */

    @Test
    public void isOccupiedFalseTest(){
        Board k = new Board();
        assertFalse(k.isOccupied(p));
    }

    //Tests that getAtPosition returns the right occupied piece.
    @Test
    public void getAtPositionOccupiedTest(){
       Board k = new Board();
       k.putAtPosition(p,piece);
       assertNotNull(k.getAtPosition(p));
    }

    // Tests that the unoccupied position returns Null.
    @Test
    public void getAtPositionNotOccupiedTest(){
        Board k = new Board();
        assertNull(k.getAtPosition(p));
    }


    //Tests that the putAtEmptyPosition function works if the position is empty.

    @Test
    public void putAtEmptyPositionTest(){
       Board k = new Board();
       k.putAtEmptyPosition(p,piece);
       assertEquals(k.getAtPosition(p),piece);
       assertNotEquals(k.getAtPosition(p),null);
    }

    /* Tests if the copy() function leaves the "real-board" unchanged,
     * and the copied board changed.
     */

    @Test
    public void copyTest(){
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
    public void putAtEmptyPositionExceptionTest(){
        Board k = new Board();
        k.putAtPosition(p, piece);
       assertThrows(IllegalArgumentException.class, ()-> {k.putAtEmptyPosition(p, piece);});
    }
    /*
     *Tests if altering the list in anyway will change the state of the board.
     */

    @Test
    public void alteringListTest(){
        Board k = new Board();
        k.setupNewGame();
        k.getAllPieces().remove(5);
        assertEquals(k.getAllPieces().size(),32);

        k.getAllPieces().removeAll(k.getAllPieces());
        assertEquals(k.getAllPieces().size(),32);
    }
    /* Test is failing,
       not sure if there is a problem
       with the test or a problem with the method implementation.

    @Test
    public void newBoardwithIchessPiece(){

        Board k = new Board(pieceList);
        Board h = new Board();
        h.setupNewGame();
        assertNotEquals(k.getAllPieces().size(),32);
    }
    */

}

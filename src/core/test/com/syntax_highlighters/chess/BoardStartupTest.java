package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class BoardStartupTest {
    private Board board;

    @Before
    private void setUp() {
        board = new Board();
        board.setupNewGame();
    }

    @Test
    void EmptySquaresAreEmpty(){
        setUp();
        boolean isEmpty = true;

        for (int y = 3; y <= 6; y++) {
            for (int x = 1; x <= 8; x++) {
                if (board.getAtPosition(new Position(x, y)) != null){
                    isEmpty = false;
                }
            }
        }
        assertEquals(isEmpty,true);

    }
    @Test
    void KingsPos(){
        setUp();
        Position wPos = new Position(5,1);
        Position bPos = new Position(5,8);

        // these can be null
        IChessPiece TestW = board.getAtPosition(wPos);
        IChessPiece TestB = board.getAtPosition(bPos);

        assertTrue(TestW instanceof ChessPieceKing);
        assertTrue(TestB instanceof ChessPieceKing);
    }
    @Test
    void QueensPos(){

    }
    @Test
    void PawnsPos(){

    }
    @Test
    void BishopsPos(){

    }
    @Test
    void KnightsPos(){

    }
    @Test
    void RooksPos(){

    }
}

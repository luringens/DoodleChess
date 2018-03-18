package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BoardStartupTest {
    Board board = new Board();

    // could use @Before, if I could get it to work...
    public void setUp() {
        board.setupNewGame();
    }

    @Test
    public void EmptySquaresAreEmpty(){
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
    public void KingsPos(){
        setUp();
        Position wPos = new Position(4,1);
        Position bPos = new Position(4,8);
        boolean IsKing = true;

        // these can be null
        IChessPiece TestW = board.getAtPosition(wPos);
        IChessPiece TestB = board.getAtPosition(bPos);

        // use "equals" for string comparison
        if (TestW.getAssetName() != "King" || TestB.getAssetName() != "King"){
            IsKing = false;
        }
        assertEquals(IsKing, true);
    }
    @Test
    public void QueensPos(){

    }
    @Test
    public void PawnsPos(){

    }
    @Test
    public void BishopsPos(){

    }
    @Test
    public void KnightsPos(){

    }
    @Test
    public void RooksPos(){

    }


}

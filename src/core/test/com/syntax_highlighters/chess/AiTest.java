package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

class AiTest {
    @Test
    /// Tests if a pawn will take a king instead of moving forwards.
    void takesKing() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceKing(new Position(3, 3), false));
        pieces.add(new ChessPiecePawn(new Position(2, 2), true));
        Board board = new Board(pieces);
        
        IAiPlayer ai = new MiniMaxAIPlayer(true, MiniMaxAIPlayer.Difficulty.Easy);
        ai.PerformMove(board);

        assertEquals(1, board.getAllPieces().size());
    }
}

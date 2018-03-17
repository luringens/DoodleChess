package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

class AiTest {
    @Test
    void takesKing() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceKing(new Position(2, 2), false));
        pieces.add(new ChessPiecePawn(new Position(1, 1), true));
        // Diagram:
        //  A B C D E F G H
        // _________________
        // |               | 8
        // |               | 7
        // |               | 6
        // |               | 5
        // |               | 4
        // |               | 3
        // | K             | 2
        // |P______________| 1
        //
        // Pawn will move up to (2, 2) aka B4 to capture the king
        Board board = new Board(pieces);
        
        IAiPlayer ai = new MiniMaxAIPlayer(true, MiniMaxAIPlayer.Difficulty.Easy);
        ai.PerformMove(board);

        assertEquals(1, board.getAllPieces().size());
    }
}

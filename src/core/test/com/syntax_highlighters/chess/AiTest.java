package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class AiTest {
    @Test
    void takesKing() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceKing(new Position(2, 2), false));
        pieces.add(new ChessPiecePawn(new Position(2, 1), true));
        Board board = new Board(pieces);

        IAiPlayer ai = new MiniMaxAIPlayer(true, MiniMaxAIPlayer.Difficulty.Easy);
        ai.PerformMove(board);

        assert(board.getAllPieces().size() == 1);
    }
}

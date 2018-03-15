package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChessPieceTest {
    @Test
    void TestRookMoves() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece rook = new ChessPieceRook(new Position(7,7), false);
        pieces.add(rook);
        Board board = new Board(pieces);

        List<Move> moves = rook.allPossibleMoves(board);
        assertEquals(14, moves.size());
    }
}

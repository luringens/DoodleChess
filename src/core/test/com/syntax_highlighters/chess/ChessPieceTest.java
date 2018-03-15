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
        IChessPiece king = new ChessPieceKing(new Position(4,4), false);
        IChessPiece pawn = new ChessPiecePawn(new Position(3,3), true);
        pieces.add(rook);
        pieces.add(king);
        pieces.add(pawn);
        Board board = new Board(pieces);

        List<Move> moves = rook.allPossibleMoves(board);
        List<Move> moves2 = king.allPossibleMoves(board);
        List<Move> moves3 = pawn.allPossibleMoves(board);
    }
}

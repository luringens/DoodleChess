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
    @Test
    void TestKingMoves() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece king = new ChessPieceKing(new Position(4,4), false);
        pieces.add(king);
        Board board = new Board(pieces);

        List<Move> moves = king.allPossibleMoves(board);
        assertEquals(8, moves.size());
    }

    @Test
    void TestBishopMoves() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceQueen(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(27, moves.size());
    }
    @Test
    void TestQueenMoves() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceQueen(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(27, moves.size());
    }
    @Test
    void TestRookMoves2() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceRook(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(14, moves.size());
    }

    @Test
    void TestRookPawn() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPiecePawn(new Position(7,7), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(2, moves.size());
    }

    @Test
    void TestRookKnight() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceKnight(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(8, moves.size());
    }

    @Test
    void TestCastling() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceKing(new Position(5,1), true);
        IChessPiece piece2 = new ChessPieceRook(new Position(8,1), true);
        IChessPiece piece3 = new ChessPieceRook(new Position(1,1), true);
        pieces.add(piece);
        pieces.add(piece2);
        pieces.add(piece3);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(7, moves.size());
    }
}

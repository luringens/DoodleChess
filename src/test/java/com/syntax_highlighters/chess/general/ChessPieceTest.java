package com.syntax_highlighters.chess.general;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.move.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.chesspiece.ChessPieceBishop;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKing;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKnight;
import com.syntax_highlighters.chess.chesspiece.ChessPiecePawn;
import com.syntax_highlighters.chess.chesspiece.ChessPieceQueen;
import com.syntax_highlighters.chess.chesspiece.ChessPieceRook;
import com.syntax_highlighters.chess.Color;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;

import org.junit.jupiter.api.Test;

/**
 * Tests asserting that the number of moves available to the different pieces is
 * as expected in different situations.
 */
class ChessPieceTest {
    @Test
    void KingHasCorrectNumberOfMovesFromCenter() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece king = new ChessPieceKing(new Position(4,4), Color.BLACK);
        pieces.add(king);
        Board board = new Board(pieces);

        List<Move> moves = king.allPossibleMoves(board);
        assertEquals(8, moves.size());
    }

    @Test
    void BishopHasCorrectNumberOfMovesFromCenter() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceBishop(new Position(4,4), Color.BLACK);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(13, moves.size());
    }
    @Test
    void QueenHasCorrectNumberOfMovesFromCenter() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceQueen(new Position(4,4), Color.BLACK);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(27, moves.size());
    }
    @Test
    void RookHasCorrectNumberOfMovesFromCenter() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceRook(new Position(4,4), Color.BLACK);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(14, moves.size());
    }

    @Test
    void PawnHasCorrectNumberOfMovesFromStartingPosition() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPiecePawn(new Position(7,7), Color.BLACK);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(2, moves.size());
    }

    @Test
    void KnightHasCorrectNumberOfMovesFromCenter() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceKnight(new Position(4,4), Color.BLACK);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(8, moves.size());
    }

    @Test
    void KingHasCorrectNumberOfMovesWhenCastlingPossibleOnBothSides() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceKing(new Position(5,1), Color.WHITE);
        IChessPiece piece2 = new ChessPieceRook(new Position(8,1), Color.WHITE);
        IChessPiece piece3 = new ChessPieceRook(new Position(1,1), Color.WHITE);
        pieces.add(piece);
        pieces.add(piece2);
        pieces.add(piece3);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(7, moves.size());
    }

    @Test
    void KingHasNoAvailableMovesWhenInCheckmate(){
        Board b = new Board();
        b.setupNewGame();

        b.movePiece(b.getAtPosition(new Position(5,2)),new Position(5,4));
        b.movePiece(b.getAtPosition(new Position(4,1)),new Position(6,3));
        b.movePiece(b.getAtPosition(new Position(6,1)),new Position(3,4));
        b.movePiece(b.getAtPosition(new Position(6,3)),new Position(6,7));

        List<Move> moves = b.getAtPosition(new Position(5,8)).allPossibleMoves(b);
        assertEquals(0, moves.size());

    }
}

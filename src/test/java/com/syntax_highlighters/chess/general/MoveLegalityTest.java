package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests designed to show that moves which should clearly be illegal are illegal.
 */
class MoveLegalityTest {

    @Test
    void HorseCannotMoveThreeStepsInStraightLine() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(3, 6);


        IChessPiece testKnight = new ChessPieceKnight(pos, Color.WHITE);
        board.putAtPosition(testKnight.getPosition(),testKnight);
        boolean IsMoveLegal = testKnight.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        System.out.println(testKnight.allPossibleMoves(board));

        assertFalse(IsMoveLegal);
    }

    @Test
    void WhitePawnCannotMoveBackwards() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(3, 2);
        IChessPiece testPawn = new ChessPiecePawn(pos, Color.WHITE);
        board.putAtPosition(testPawn.getPosition(),testPawn);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        assertFalse(IsMoveLegal);
    }

    @Test
    void BlackPawnCannotMoveBackwards() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(3, 4);

        IChessPiece testPawn = new ChessPiecePawn(pos, Color.BLACK);
        board.putAtPosition(testPawn.getPosition(),testPawn);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        assertFalse(IsMoveLegal);
    }

    @Test
    void PawnCannotAttackEmptySquare() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(4, 4);

        IChessPiece testPawn = new ChessPiecePawn(pos, Color.WHITE);
        board.putAtPosition(testPawn.getPosition(),testPawn);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        assertFalse(IsMoveLegal);
    }

    @Test
    void PieceCannotAttackAlly() {
        Board board = new Board();

        Position AtkPos = new Position(3, 3);
        Position DefPos = new Position(4, 4);

        IChessPiece testQueen = new ChessPieceQueen(AtkPos, Color.WHITE);
        IChessPiece testPawn = new ChessPiecePawn(DefPos, Color.WHITE);
        board.putAtPosition(testQueen.getPosition(), testQueen);
        board.putAtPosition(testPawn.getPosition(), testPawn);

        boolean IsMoveLegal = testQueen.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(DefPos));
        System.out.println(IsMoveLegal);
        assertFalse(IsMoveLegal);
    }

}

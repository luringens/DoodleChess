package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import javafx.geometry.Pos;
import org.junit.jupiter.api.Test;
import com.syntax_highlighters.chess.Move;
import sun.font.CompositeStrike;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;


public class MoveLegalityTest {

    @Test
    public void HorseThreeStraightCheckMove() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(3, 6);


        IChessPiece testKnight = new ChessPieceKnight(pos, true);
        board.putAtPosition(testKnight.getPosition(),testKnight);
        boolean IsMoveLegal = testKnight.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        System.out.println(testKnight.allPossibleMoves(board));

        assertFalse(IsMoveLegal);
    }

    @Test
    public void WhitePawnBackwards() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(3, 2);
        IChessPiece testPawn = new ChessPiecePawn(pos, true);
        board.putAtPosition(testPawn.getPosition(),testPawn);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        assertFalse(IsMoveLegal);
    }

    @Test
    public void BlackPawnBackwards() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(3, 4);

        IChessPiece testPawn = new ChessPiecePawn(pos, false);
        board.putAtPosition(testPawn.getPosition(),testPawn);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        assertFalse(IsMoveLegal);
    }

    @Test
    public void PawnAttackEmptySquare() {
        Board board = new Board();

        Position pos = new Position(3, 3);
        Position newPos = new Position(4, 4);

        IChessPiece testPawn = new ChessPiecePawn(pos, true);
        board.putAtPosition(testPawn.getPosition(),testPawn);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(newPos));

        assertFalse(IsMoveLegal);
    }

    @Test
    public void PieceAttackAlly() {
        Board board = new Board();

        Position AtkPos = new Position(3, 3);
        Position DefPos = new Position(4, 4);

        IChessPiece testQueen = new ChessPieceQueen(AtkPos, true);
        IChessPiece testPawn = new ChessPiecePawn(DefPos, true);
        board.putAtPosition(testQueen.getPosition(), testQueen);
        board.putAtPosition(testPawn.getPosition(), testPawn);

        boolean IsMoveLegal = testQueen.allPossibleMoves(board).stream().anyMatch(m -> m.getPosition().equals(DefPos));
        System.out.println(IsMoveLegal);
        assertFalse(IsMoveLegal);
    }

}
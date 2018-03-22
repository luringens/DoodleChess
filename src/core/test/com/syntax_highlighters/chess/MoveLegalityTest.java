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
    public void HorseThreeStraightCheckMove(){
        Board board = new Board();

        Position pos = new Position(3,3);
        Position newPos = new Position(4,5);

        IChessPiece testKnight = new ChessPieceKnight(pos,true);

        boolean IsMoveLegal = testKnight.allPossibleMoves(board).contains(newPos);
        System.out.println(testKnight.allPossibleMoves(board));

        assertFalse(IsMoveLegal);
    }
    @Test
    public void WhitePawnBackwards(){
        Board board = new Board();

        Position pos = new Position(3,3);
        Position newPos = new Position (3,2);
        IChessPiece testPawn = new ChessPiecePawn(pos,true);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).contains(newPos);

        assertFalse(IsMoveLegal);
    }
    @Test
    public void BlackPawnBackwards (){
        Board board = new Board();

        Position pos = new Position(3,3);
        Position newPos = new Position (3,4);

        IChessPiece testPawn = new ChessPiecePawn(pos,false);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).contains(newPos);

        assertFalse(IsMoveLegal);
    }
    @Test
    public void PawnAttackEmptySquare(){
        Board board = new Board();

        Position pos = new Position(3,3);
        Position newPos = new Position(4,4);

        IChessPiece testPawn = new ChessPiecePawn(pos,true);

        boolean IsMoveLegal = testPawn.allPossibleMoves(board).contains(newPos);

        assertFalse(IsMoveLegal);
    }
    @Test
    public void PieceAttackAlly() {
        Board board = new Board();

        Position AtkPos = new Position(3,3);
        Position DefPos = new Position(4,4);

        IChessPiece testQueen = new ChessPieceQueen(AtkPos, true);
        IChessPiece testPawn = new ChessPiecePawn(DefPos, true);

        boolean IsMoveLegal = testQueen.allPossibleMoves(board).contains(DefPos);

        assertFalse(IsMoveLegal);
    }

    @Test
    public void EnPassant () {
        //setup board.
        Board board = new Board();
        board.setupNewGame();
        IChessPiece testPawn;

        //Do moves to place pawns into enpassant situation.
        Move firstMove = new Move(new Position(6,2),new Position(6,4),board.getAtPosition(new Position(6,2)));
        firstMove.DoMove(board);
        Move secondMove = new Move(new Position(1,7),new Position(1,7),board.getAtPosition(new Position(1,7)));
        secondMove.DoMove(board);
        Move thirdMove = new Move(new Position(6,4),new Position(6,5),board.getAtPosition(new Position(6,4)));
        thirdMove.DoMove(board);
        Move fourthMove = new Move(new Position(6,7),new Position(6, 5),board.getAtPosition(new Position(6,7)));
        fourthMove.DoMove(board);
        testPawn = board.getAtPosition(new Position(6,5));

        // check if the enpassant move position is among the legal moves the piece can perform.
        assertTrue(testPawn.allPossibleMoves(board).contains(new Position(7,6)));
    }
}
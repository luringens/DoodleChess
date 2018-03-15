package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MoveLegalityTest {
    Board Board = new Board ();
    Position pos = new Position(3,3);


    @Test
    public void HorseThreeStraightCheckMove(){
        Position newPos = new Position(4,5);
        IChessPiece testKnight = new ChessPieceKnight(pos,true);

        boolean IsMoveLegal = testKnight.allPossibleMoves(Board).contains(newPos);
        System.out.println(testKnight.allPossibleMoves(Board));

        assertEquals(IsMoveLegal,true);
    }
    @Test
    public void WhitePawnBackwards(){
        Position newPos = new Position (3,2);
        IChessPiece testPawn = new ChessPiecePawn(pos,true);

        boolean IsMoveLegal = testPawn.allPossibleMoves(Board).contains(newPos);

        assertEquals(IsMoveLegal,false);
    }
    @Test
    public void BlackPawnBackwards (){
        Position newPos = new Position (3,4);
        IChessPiece testPawn = new ChessPiecePawn(pos,false);

        boolean IsMoveLegal = testPawn.allPossibleMoves(Board).contains(newPos);

        assertEquals(IsMoveLegal,false);

    }
    @Test
    public void PawnAttackEmptySquare(){

        Position newPos = new Position(4,4);
        IChessPiece testPawn = new ChessPiecePawn(pos,true);

        boolean IsMoveLegal = testPawn.allPossibleMoves(Board).contains(newPos);
    }

    public void PieceAttackAlly(){

        IChessPiece testQueen = new ChessPieceQueen(newPos,true)
        Position newPos = new Position(6,6)

    }

    public void PieceMoveLength () {

    }

    public void EnPassant () {

    }

    public void Castling() {

    }





}

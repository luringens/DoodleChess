package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardStartupTest {

    private Board board;

    @Before
    private void setUp() {
        board = new Board();
        board.setupNewGame();
    }

    @Test
    void EmptySquaresAreEmpty() {
        setUp();
        boolean isEmpty = true;
        Position pos;
        for (int y = 3; y <= 6; y++) {
            for (int x = 1; x <= 8; x++) {
                pos = new Position(x, y);
                if (board.getAtPosition(pos) != null) {
                    isEmpty = false;
                }
            }
        }
        assertEquals(isEmpty, true);

    }

    @Test
    void KingsPos(){
        setUp();
        Position wPos = new Position(5, 1);
        Position bPos = new Position(5, 8);

        // these can be null
        IChessPiece TestW = board.getAtPosition(wPos);
        IChessPiece TestB = board.getAtPosition(bPos);

        assertTrue(TestW instanceof ChessPieceKing);
        assertTrue(TestB instanceof ChessPieceKing);
    }

    @Test
    void QueensPos() {
        setUp();
        Position wPos = new Position(4, 1);
        Position bPos = new Position(4, 8);

        IChessPiece TestW = board.getAtPosition(wPos);
        IChessPiece TestB = board.getAtPosition(bPos);
        System.out.println(TestW.getAssetName());
        assertTrue(TestW instanceof ChessPieceQueen);
        assertTrue(TestB instanceof ChessPieceQueen);
    }

    @Test
    void PawnsPos() {
        setUp();
        Position Wpos;
        Position Bpos;

        for (int x = 1; x <= 8; x++) {
            Wpos = new Position(x, 2);

            IChessPiece TestW = board.getAtPosition(Wpos);
            assertTrue(TestW instanceof ChessPiecePawn);
        }
        for (int x = 1; x <= 8; x++) {
            Bpos = new Position(x, 7);

            IChessPiece TestB = board.getAtPosition(Bpos);
            assertTrue(TestB instanceof ChessPiecePawn);
        }
    }

    @Test
    void BishopsPos() {
        setUp();
        Position BleftPos = new Position(3, 8);
        Position BrightPos = new Position(6, 8);
        Position WrightPos = new Position(3, 1);
        Position WleftPos = new Position(6, 1);

        IChessPiece BrightBish = board.getAtPosition(BleftPos);
        IChessPiece BleftBish = board.getAtPosition(BrightPos);
        IChessPiece WrightBish = board.getAtPosition(WrightPos);
        IChessPiece WleftBish = board.getAtPosition(WleftPos);

        assertTrue(BrightBish instanceof ChessPieceBishop);
        assertTrue(BleftBish instanceof ChessPieceBishop);
        assertTrue(WrightBish instanceof ChessPieceBishop);
        assertTrue(WleftBish instanceof ChessPieceBishop);
    }

    @Test
    void KnightsPos() {
        setUp();

        Position BleftPos = new Position(2, 8);
        Position BrightPos = new Position(7, 8);
        Position WrightPos = new Position(2, 1);
        Position WleftPos = new Position(7, 1);

        IChessPiece BrightBish = board.getAtPosition(BleftPos);
        IChessPiece BleftBish = board.getAtPosition(BrightPos);
        IChessPiece WrightBish = board.getAtPosition(WrightPos);
        IChessPiece WleftBish = board.getAtPosition(WleftPos);

        assertTrue(BrightBish instanceof ChessPieceKnight);
        assertTrue(BleftBish instanceof ChessPieceKnight);
        assertTrue(WrightBish instanceof ChessPieceKnight);
        assertTrue(WleftBish instanceof ChessPieceKnight);
    }

    @Test
    void RooksPos() {
        setUp();
        Position BleftPos = new Position(1, 8);
        Position BrightPos = new Position(8, 8);
        Position WrightPos = new Position(1, 1);
        Position WleftPos = new Position(8, 1);

        IChessPiece BrightBish = board.getAtPosition(BleftPos);
        IChessPiece BleftBish = board.getAtPosition(BrightPos);
        IChessPiece WrightBish = board.getAtPosition(WrightPos);
        IChessPiece WleftBish = board.getAtPosition(WleftPos);

        assertTrue(BrightBish instanceof ChessPieceRook);
        assertTrue(BleftBish instanceof ChessPieceRook);
        assertTrue(WrightBish instanceof ChessPieceRook);
        assertTrue(WleftBish instanceof ChessPieceRook);

    }
    /*
    @Test
    public void BlackCannotMoveFirst(){
        Position oldPos = new Position(7,1);
        Position newPos = new Position(6,3);
        IChessPiece testKnight = new ChessPieceKnight(oldPos,false);

        Move move = new Move(oldPos,newPos,testKnight);

        //TODO: Find out how this works .. :P
        //assertThrows(RuntimeException.class, move.DoMove(board));
        }*/
}

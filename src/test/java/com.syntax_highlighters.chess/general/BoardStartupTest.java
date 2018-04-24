package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the initial board setup, checks that the preconditions for a new
 * game of chess holds.
 */
class BoardStartupTest {

    private Board board;

    // NOTE: Although this is marked using @Before, it doesn't work on my
    // system. It requires the method to be called in every test method.
    // - Vegard
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

        // Go through the middle positions of the board, ensure those positions
        // are empty
        for (int y = 3; y <= 6; y++) {
            for (int x = 1; x <= 8; x++) {
                pos = new Position(x, y);
                if (board.getAtPosition(pos) != null) {
                    isEmpty = false;
                }
            }
        }
        assertEquals(true, isEmpty);

    }

    @Test
    void KingsArePositionedCorrectly(){
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
    void QueensArePositionedCorrectly() {
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
    void PawnsArePositionedCorrectly() {
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
    void BishopsArePositionedCorrectly() {
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
    void KnightsArePositionedCorrectly() {
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
    void RooksArePositionedCorrectly() {
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

    @Test
    void BlackCannotMoveFirst(){
        ChessGame game = new ChessGame(null,null);
        board = game.getBoard();

        IChessPiece testPawn = board.getAtPosition(new Position(1,7));


        assertEquals(0, game.performMove(new Position(1,7),new Position(1,6)).size());
        assertEquals(new Position(1,7), testPawn.getPosition());
    }
}

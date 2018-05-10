package com.syntax_highlighters.chess.general;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.game.BurningChess;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.chesspiece.ChessPiecePawn;
import com.syntax_highlighters.chess.Color;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;

import org.junit.jupiter.api.Test;

class burningchessTest {
    private BurningChess burning;
    private final Board b = new Board();

    @Test
    void allPossibleMovesTest() {
        burning = BurningChess.setupTestBoard(b, Color.WHITE);
        Board bo = burning.getBoard();
        IChessPiece piece = new ChessPiecePawn(new Position(2, 2), Color.WHITE);
        bo.putAtPosition(new Position(2, 2), piece);
        burning.killTile(new Position(2, 3));
        assertEquals(1, burning.allPossibleMoves(piece).size());

    }

    @Test
    void killPieceTest(){
        burning = burning.setupTestBoard(b,Color.WHITE);
        Board bo = burning.getBoard();
        IChessPiece piece = new ChessPiecePawn(new Position(2,2),Color.WHITE);
        bo.putAtPosition(new Position(2,2),piece);
        System.out.println(bo);
        burning.killPiece(piece);
        assertEquals(0,burning.getBoard().getAllPieces().size());
    }

    @Test
    void revivePieceTest(){
        burning = burning.setupTestBoard(b, Color.WHITE);
        Board bo = burning.getBoard();
        IChessPiece piece = new ChessPiecePawn(new Position(2, 2), Color.WHITE);
        bo.putAtPosition(new Position(2, 2), piece);
        burning.killTile(new Position(2, 3));
        assertEquals(1,burning.tileUnreachable().size());
        burning.reviveTile(new Position(2,3));
        assertEquals(0,burning.tileUnreachable().size());
    }




}

package com.syntax_highlighters.chess.general;

import com.badlogic.gdx.Game;
import com.syntax_highlighters.chess.BurningChess;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.ChessPiecePawn;
import com.syntax_highlighters.chess.entities.Color;
import com.syntax_highlighters.chess.entities.IChessPiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class burningchessTest {
    BurningChess burning;
    Board b = new Board();
    List<Position> l = new ArrayList<>();

    @Test
    public void allPossibleMovesTest() {
        burning = burning.setupTestBoard(b, Color.WHITE);
        Board bo = burning.getBoard();
        IChessPiece piece = new ChessPiecePawn(new Position(2, 2), Color.WHITE);
        bo.putAtPosition(new Position(2, 2), piece);
        burning.killTile(new Position(2, 3));
        assertEquals(burning.allPossibleMoves(piece).size(), 1);

    }

    @Test
    public void killPieceTest(){
        burning = burning.setupTestBoard(b,Color.WHITE);
        Board bo = burning.getBoard();
        IChessPiece piece = new ChessPiecePawn(new Position(2,2),Color.WHITE);
        bo.putAtPosition(new Position(2,2),piece);
        System.out.println(bo);
        burning.killPiece(piece);
        assertEquals(burning.getBoard().getAllPieces().size(),0);
    }

    @Test
    public void revivePieceTest(){
        burning = burning.setupTestBoard(b, Color.WHITE);
        Board bo = burning.getBoard();
        IChessPiece piece = new ChessPiecePawn(new Position(2, 2), Color.WHITE);
        bo.putAtPosition(new Position(2, 2), piece);
        burning.killTile(new Position(2, 3));
        assertEquals(burning.tileUnreachable().size(),1);
        burning.reviveTile(new Position(2,3));
        assertEquals(burning.tileUnreachable().size(),0);
    }




}

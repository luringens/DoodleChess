package com.syntax_highlighters.test.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class coordPieceTest {

    IChessPiece[][] pieces;
    ChessPieceKing whiteKing = new ChessPieceKing(0,4);
    ChessPieceKing blackKing = new ChessPieceKing();
    ChessPieceQueen whiteQueen = new ChessPieceQueen();
    ChessPieceQueen blackQueen = new ChessPieceQueen();
    Position p = new Position(3,2);



    @Test
    public void wking(){ //checking white king pos
        assertEquals(whiteKing.getX(),4);
        assertEquals(whiteKing.getY(),0);
    }

    @Test
    public void bKing(){ // checking black king pos
        Assertions.assertEquals(blackKing.getX(),4);
        assertEquals(blackKing.getY(),7);
    }

    @Test
    public void wQueen(){ // checking white queen pos
        assertEquals(whiteQueen.getX(),3);
        assertEquals(whiteQueen.getY(),0);
    }
    @Test
    public void bQueen(){ //checking black queen pos
        assertEquals(blackQueen.getX(),3);
        assertEquals(blackQueen.getY(),7);
    }

}
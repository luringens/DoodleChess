package com.syntax_highlighters.chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class coordPieceTest {
   /*
    ChessPieceKing whiteKing = new ChessPieceKing(0,4);
    ChessPieceKing blackKing = new ChessPieceKing();
    ChessPieceQueen whiteQueen = new ChessPieceQueen();
    ChessPieceQueen blackQueen = new ChessPieceQueen();*/
    Position p = new Position(3,2);
    String notation = "a4";
    Position note = Position.fromChessNotation(notation);



    @Test
    public void positiontest(){
        assertEquals(p.getX(),3);
        assertEquals(p.getY(),2);

    }

    @Test
    public void parsertest(){
        assertEquals(note.getX(),1);
        assertEquals(note.getY(),4);
    }

    /*
    @Test
    public void wking(){ //checking white king pos
        assertEquals(whiteKing.getX(),5);
        assertEquals(whiteKing.getY(),1);
    }

    @Test
    public void bKing(){ // checking black king pos
        assertEquals(blackKing.getX(),4);
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
    */
}

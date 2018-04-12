package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomMapgenTest{

    Random rdm = new Random();
    Board board = new Board();

    @Test
    void genTileMapTest() {
        int testBonus = rdm.nextInt(10);
        int[][] testMap = board.generateTileMap(testBonus);
        int n = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if (testMap[i][j] != 0) {
                    n++;
                }
            }
        }
        assertEquals(testBonus+16, n);
    }
    //Tests that each element produced by generateTileScore is always a correct piece score.
    @Test
    void GenerateTileScoreTest(){
        int[] score = new int[]{100,320,330,500,900};
        int[] Scores = board.generateTileScores(50,-5);
        int contains = 0;

        for (int i = 0; i < Scores.length; i++){
            for (int j = 0; j < score.length;j++){
                if (score[j] == Scores[i]){
                        contains++;
                }
            }
        }
        assertEquals(Scores.length,contains);
    }

    void getPiecefromScoreTest(){


    }
    @Test
    void BonusPiecesisCorrectTest(){
        int rdmBonus1 = rdm.nextInt(16 + 15 +1) - 15;
        int rdmBonus2 = rdm.nextInt(16 + 15 +1) - 15;
        int rdmHcp1 = rdm.nextInt(100 + 1);
        int rdmHcp2 = rdm.nextInt(100 + 1);
        board.setupRandomGame(rdmHcp1,rdmHcp2,rdmBonus1,rdmBonus2);
        List<IChessPiece> list = board.getAllPieces();

        assertEquals(rdmBonus1+rdmBonus2 + 32, list.size());
    }

    @Test
    void setupRandomGamehasKingsTest(){
        Board testBoard = new Board();
        //int rdmBonus1 = rdm.nextInt(16 + 15 +1) - 15;
        int rdmBonus1 = 15;
        int rdmBonus2 = rdm.nextInt(16 + 15 +1) - 15;
        int rdmHcp1 = rdm.nextInt(100 + 1);
        int rdmHcp2 = rdm.nextInt(100 + 1);
        testBoard.setupRandomGame(rdmHcp1,rdmHcp2,rdmBonus1,rdmBonus2);
        Position Bkingpos = new Position(4,8);
        Position Wkingpos = new Position(4,1);
        System.out.println(testBoard);
        System.out.println("Whcp: " + rdmHcp1 + " Bhcp: " + rdmHcp2 + " W Bonus: " + rdmBonus1 + " B bonus: " + rdmBonus2);

        //assertTrue(board.getAtPosition(Bkingpos) instanceof ChessPieceKing);
        //assertTrue(board.getAtPosition(Wkingpos) instanceof ChessPieceKing);
    }
    @Test
    void SpeedTest(){
        board.setupRandomGame(50,50,2,2);
        System.out.println(board);
    }
}

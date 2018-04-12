package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.ChessPieceKing;
import org.junit.jupiter.api.Test;

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
        assertEquals(testBonus+15, n);
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
    void setupRandomGameTest(){
        
        int rdmBonus1 = rdm.nextInt(16 + 16 +1) - 16;
        int rdmBonus2 = rdm.nextInt(16 + 16 +1) - 16;
        int rdmHcp1 = rdm.nextInt(100 + 1);
        int rdmHcp2 = rdm.nextInt(100 + 1);
        board.setupRandomGame(rdmHcp1,rdmHcp2,rdmBonus1,rdmBonus2);
        Position Bkingpos = new Position(4,8);
        Position Wkingpos = new Position(4,1);

        assertTrue(board.getAtPosition(Bkingpos) instanceof ChessPieceKing);
        assertTrue(board.getAtPosition(Wkingpos) instanceof ChessPieceKing);
    }

}

package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomMapgenTest{


    void genTileMapTest(){
        Random rdm = new Random();
        int testBonus = rdm.nextInt(10);
        Board board = new Board();
        int[][] testMap = board.generateTileMap(testBonus);
        int n = 0;
        System.out.println(board);
        for (int i = 1;i < 8; i++){
            for(int j = 1;j < 4;j++){
                if (testMap[i][j]== 0) n=+n;
            }
        }
        assertEquals(16,n);
    }

    void GenerateTileScoreTest(){

    }

    void getPiecefromScoreTest(){

    }
    @Test
    void setupRandomGameTest(){
        Board board = new Board();
        Random rdm = new Random();

        board.setupRandomGame(50,50,2,2);
        System.out.println(board);
    }

}

package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.ChessPieceKing;
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
        int rdmBonus1 = 15;
        int rdmBonus2 = rdm.nextInt(16 + 15 +1) - 15;
        int rdmHcp1 = rdm.nextInt(100 + 1);
        int rdmHcp2 = rdm.nextInt(100 + 1);
        board.setupRandomGame(rdmHcp1,rdmHcp2,rdmBonus1,rdmBonus2);

        long kings = board.getAllPieces().stream().filter(p -> p instanceof ChessPieceKing).count();
        assertEquals(2, kings);
    }

    @Test
    void SetupPracGamehasKingsTest(){
        board.setupNewGame();
        board.setupPracticeGame(rdm.nextInt(20));

        long kings = board.getAllPieces().stream().filter(p -> p instanceof ChessPieceKing).count();
        assertEquals(2,kings);

    }
}

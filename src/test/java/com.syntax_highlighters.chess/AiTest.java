package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Tests pertaining to the behavior of the AI.
 */
class AiTest {
    @Test
    void takesQueenOverPawn() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece queen = new ChessPieceQueen(new Position(3, 3), false);
        pieces.add(queen);
        pieces.add(new ChessPiecePawn(new Position(1, 3), false));
        pieces.add(new ChessPiecePawn(new Position(2, 2), true));

        // Add kings so the AI doesn't think it's Game Over.
        pieces.add(new ChessPieceKing(new Position(1, 8), false));
        pieces.add(new ChessPieceKing(new Position(8, 8), true));

        Board board = new Board(pieces);
        
        IAiPlayer ai = new MiniMaxAIPlayer(true, AiDifficulty.Hard);
        ai.PerformMove(board);

        assertFalse(board.getAllPieces().contains(queen));
    }

    @Test
    void easyAICompletesWithinOneSecondFromStartingPosition() {
        final long allowedTime = 1000;
        IAiPlayer ai = new MiniMaxAIPlayer(true, AiDifficulty.Easy);
        long time = speedTest(ai);
        assertTrue("The easy AI is too slow (" + time + " >= " + allowedTime + ")",
                time < allowedTime);
    }

    @Test
    void mediumAICompletesWithinThreeSecondsFromStartingPosition() {
        final long allowedTime = 3000;
        IAiPlayer ai = new MiniMaxAIPlayer(true, AiDifficulty.Medium);
        long time = speedTest(ai);
        assertTrue("The medium AI is too slow (" + time + " >= " + allowedTime + ")",
                time < allowedTime);
    }
    
    @Test
    void hardAICompletesWithinThreeSecondsFromStartingPosition() {
        final long allowedTime = 3000;
        IAiPlayer ai = new MiniMaxAIPlayer(true, AiDifficulty.Hard);
        long time = speedTest(ai);
        assertTrue("The hard AI is too slow (" + time + " >= " + allowedTime + ")",
                time < allowedTime);
    }

    /** Measures how long an AI spends deciding a move on a fresh chess board.
     * @param ai The AI to measure.
     * @return The number of millseconds spent.
     */
    private long speedTest(IAiPlayer ai) {
        Board board = new Board();
        board.setupNewGame();

        long start = System.nanoTime();
        ai.PerformMove(board);
        long end = System.nanoTime();

        return (end - start) / 1_000_000;
    }
}

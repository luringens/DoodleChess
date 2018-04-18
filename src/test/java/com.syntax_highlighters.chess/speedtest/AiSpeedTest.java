package com.syntax_highlighters.chess.speedtest;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests pertaining to the behavior of the AI.
 */
class AiSpeedTest {
    @Test
    void easyAICompletesWithinOneSecondFromStartingPosition() {
        final long allowedTime = 1000;
        IAiPlayer ai = new MiniMaxAIPlayer(Color.WHITE, AiDifficulty.Easy);
        long time = speedTest(ai);
        assertTrue("The easy AI is too slow (" + time + " >= " + allowedTime + ")",
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

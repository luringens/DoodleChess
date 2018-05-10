package com.syntax_highlighters.chess.general;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.entities.AiDifficulty;

import org.junit.jupiter.api.Test;

/**
 * Tests pertaining to the Async Player Wrapper.
 */
class AsyncPlayerTest {
    /**
     * Check that it can generate two moves.
     */
    @Test
    void canCreateTwoAsyncMoves() throws Exception {
        ChessGame game = new ChessGame(AiDifficulty.Easy, AiDifficulty.Easy);

        game.PerformAIMoveAsync();
        Thread.sleep(1000);
        Move m1 = game.PerformAIMoveAsync();
        assertNotNull(m1);

        game.PerformAIMoveAsync();
        Thread.sleep(1000);
        Move m2 = game.PerformAIMoveAsync();
        assertNotNull(m2);

        System.out.println(m1 + "\n" + m2);
    }
}

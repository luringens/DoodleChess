package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.ai.AiDifficulty;
import com.syntax_highlighters.chess.game.ChessGame;
import com.syntax_highlighters.chess.move.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

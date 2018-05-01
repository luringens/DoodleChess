package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

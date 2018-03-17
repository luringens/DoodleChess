package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

class AiTest {
    @Test
    /// Tests if a pawn will take a king instead of moving forwards.
    void takesKing() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceKing(new Position(3, 3), false));
        pieces.add(new ChessPiecePawn(new Position(2, 2), true));
        Board board = new Board(pieces);
        
        IAiPlayer ai = new MiniMaxAIPlayer(true, MiniMaxAIPlayer.Difficulty.Easy);
        ai.PerformMove(board);

        assertEquals(1, board.getAllPieces().size());
    }

    @Test
    void speedTestEasy() {
        final long allowedTime = 1000;
        IAiPlayer ai = new MiniMaxAIPlayer(true, MiniMaxAIPlayer.Difficulty.Easy);
        long time = speedTest(ai);
        System.out.print("Expected less than: " + allowedTime + ", actual: " + time);
        assertTrue("The easy AI is too slow.", time < allowedTime);
    }

    @Test
    void speedTestMedium() {
        final long allowedTime = 3000;
        IAiPlayer ai = new MiniMaxAIPlayer(true, MiniMaxAIPlayer.Difficulty.Medium);
        long time = speedTest(ai);
        System.out.print("Expected less than: " + allowedTime + ", actual: " + time);
        assertTrue("The easy AI is too slow.", time < allowedTime);
    }

    @Test
    void speedTestHard() {
        final long allowedTime = 5000;
        IAiPlayer ai = new MiniMaxAIPlayer(true, MiniMaxAIPlayer.Difficulty.Hard);
        long time = speedTest(ai);
        System.out.print("Expected less than: " + allowedTime + ", actual: " + time);
        assertTrue("The easy AI is too slow.", time < allowedTime);
    }

    /** Measures how long an AI spends deciding a move on a fresh chess board.
     * @param ai The AI to measure.
     * @return The number of millseconds spent.
     */
    long speedTest(IAiPlayer ai) {
        Board board = new Board();
        board.setupNewGame();

        long start = System.nanoTime();
        ai.PerformMove(board);
        long end = System.nanoTime();

        return (end - start) / 1_000_000;
    }
}

package com.syntax_highlighters.chess.entities;
import com.syntax_highlighters.chess.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A chess AI trying to find the best move by using a minimaxing algorithm.
 *
 * The AI attempts to simulate many different moves for a certain number of
 * turns ahead, in order to determine which move is the best move to make in a
 * given situation. The difficulty setting of the AI determines how far it
 * attempts to look ahead.
 *
 * In order to determine the best move to make, the AI recursively simulates the
 * best move by the next player until it has reached the depth it is allowed to
 * look ahead. How much a move is worth is determined by which pieces are
 * located where on the board, and the AI selects the move which leads to the
 * highest possible score given an initial distribution of pieces.
 *
 * This AI also uses alpha-beta pruning, which is a technique used to limit the
 * number of branches on the move tree you need to explore by eliminating
 * branches which obviously result in bad scores as early as possible, in order
 * to drastically decrease the running time of the algorithm.
 */
public class MiniMaxAIPlayer implements IAiPlayer {
    private static final int EASY_DEPTH = 3;
    private static final int MED_DEPTH = 4;
    private static final int HARD_DEPTH = 5;
    private final boolean isWhite;
    private int diff;
    private Random rand;

    /**
     * Create a minimaxing AI player with the given color and difficulty.
     *
     * @param isWhite Whether or not the AI player plays with the white pieces
     * @param diff The difficulty setting of the AI
     */
    public MiniMaxAIPlayer(boolean isWhite, AiDifficulty diff) {
        this.isWhite = isWhite;
        this.SetDifficulty(diff);
        rand = new Random(); // add a certain random element to avoid AI vs. AI repetition
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void PerformMove(Board board) {
        if (board.checkMate(isWhite)) return;
        MiniMaxMove(diff, board, isWhite);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the enum value is not one of Easy,
     * Medium or Hard
     */
    @Override
    public void SetDifficulty(AiDifficulty diff) {
        switch (diff) {
            case Easy: this.diff = EASY_DEPTH; break;
            case Medium: this.diff = MED_DEPTH; break;
            case Hard: this.diff = HARD_DEPTH; break;
            default: throw new IllegalArgumentException("Invalid enum.");
        }
    }
    
    /**
     * Perform the best possible move you can make, assuming the game ends after
     * depth number of moves.
     *
     * Modifies the board state by making the move it deems the "best move".
     * 
     * @param depth The number of moves to look ahead
     * @param board The current board state
     * @param isWhite Whether or not the AI player plays with the white pieces
     */
    private void MiniMaxMove(int depth, Board board, boolean isWhite) {
        assert(depth >= 1);
        List<Move> moves = board.getAllPieces().stream()
                .filter(p -> p.isWhite() == isWhite)
                .flatMap(p -> p.allPossibleMoves(board).stream())
                .collect(Collectors.toList());

        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        for (Move move : moves) {
            move.DoMove(board);
            int score = MiniMaxScore(depth - 1, board,
                    isWhite, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            move.UndoMove(board);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        if (bestMove != null) bestMove.DoMove(board);
    }

    /**
     * Recursively determine the score a given move will give by alternatingly
     * running minimizing and maximizing for a given number of moves, until
     * there are no moves left to make.
     *
     * This method uses alpha-beta pruning.
     *
     * @param depth The remaining depth to consider
     * @param board The board state at this stage of the lookahead
     * @param isWhite Whether or not the AI player plays with the white pieces
     * @param isMaximizing Whether the player should maximize or minimize at
     * this point
     * @param alpha The current alpha value
     * @param beta The current beta value
     *
     * @return The score the board will result in assuming both players play
     * optimally for depth number of moves
     */
    private int MiniMaxScore(int depth, Board board, boolean isWhite, boolean isMaximizing, int alpha, int beta) {
        if (depth <= 0) return evaluateScore(board, isWhite);
        List<Move> moves = board.getAllPieces().stream()
                .filter(p -> p.isWhite() == (isWhite == isMaximizing))
                .flatMap(p -> p.allPossibleMoves(board).stream())
                .collect(Collectors.toList());

        if (moves.size() == 0) return evaluateScore(board, isWhite);

        // NOTE: code duplication; consider refactoring
        if (isMaximizing) {
            for (Move move : moves) {
                // Check if the move takes the king.
                if (move.getPosition() == board.getKing(isWhite)) return 10000;

                move.DoMove(board);
                int score = MiniMaxScore(depth - 1, board, isWhite, false, alpha, beta);
                move.UndoMove(board);

                // Alpha-beta pruning - early return for optimization
                if (score >= beta) return beta;
                alpha = Math.max(alpha, score);
            }
            return alpha;
        }
        else {
            for (Move move : moves) {
                // Check if the move takes the king.
                if (move.getPosition() == board.getKing(!isWhite)) return -10000;

                move.DoMove(board);
                int score = MiniMaxScore(depth - 1, board, isWhite, true, alpha, beta);
                move.UndoMove(board);

                // Alpha-beta pruning - early return for optimization
                if (score <= alpha) return alpha;
                beta = Math.min(beta, score);
            }
            return beta;
        }
    }

    /**
     * Helper method: determine the score of the board for a given player.
     *
     * @param board The current board state
     * @param forWhite Whether to determine score for white or black player
     *
     * @return The score of the board for the given player
     */
    private int evaluateScore(Board board, boolean forWhite) {
        int score = rand.nextInt(10) - 5;
        for (IChessPiece p : board.getAllPieces()) {
            if (p.isWhite() == forWhite) score += p.getPositionalScore();
            else score -= p.getPieceScore();
        }
        return score;
    }
}

package com.syntax_highlighters.chess.entities;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.syntax_highlighters.chess.AbstractGame;
import com.syntax_highlighters.chess.Move;

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
    private static final int HARD_DEPTH = 4;
    private static final int SHORTSIGHTED_DEPTH = 3;
    private int diff;
    private final Random rand;
    private double chanceOfMistake = 0.0;

    /**
     * Create a minimaxing AI player with the given color and difficulty.
     *
     * @param color Whether or not the AI player plays with the white pieces
     * @param diff The difficulty setting of the AI
     */
    public MiniMaxAIPlayer(AiDifficulty diff) {
        this.SetDifficulty(diff);
        rand = new Random(); // add a certain random element to avoid AI vs. AI repetition
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void PerformMove(AbstractGame game) {
        Move m = GetMove(game);
        if (m != null) m.DoMove(game.getBoard());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move GetMove(AbstractGame game) {
        if (game.getBoard().checkMate(game.nextPlayerColor())) return null;
        return MiniMaxMove(diff, game);
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
            case Easy: this.diff = EASY_DEPTH; chanceOfMistake = 0.2; break;
            case Medium: this.diff = MED_DEPTH; chanceOfMistake = 0.1; break;
            case Hard: this.diff = HARD_DEPTH; break;
            case ShortSighted: this.diff = SHORTSIGHTED_DEPTH; break;
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
     * @return The suggested best move.
     */
    private Move MiniMaxMove(int depth, AbstractGame game) {
        assert(depth >= 1);
        List<IChessPiece> pieces = game.getBoard().getAllPieces();

        boolean easy = depth == EASY_DEPTH;
        boolean med = depth == MED_DEPTH;

        // Increase search depth when few pieces remain, unless set to easy.
        if (!easy) {
            if (pieces.size() < 15) depth++;
            if (pieces.size() < 10) depth++;

            // Don't become unstoppable late-game unless set to hard.
            if (!med) {
                if (pieces.size() < 7) depth++;
                if (pieces.size() < 5) depth += 3;
            }
        }
        int finalDepth = depth; // Stupid Java lambda thing

        // Get all possible first moves for the AI.
        List<Move> moves = game.getPossibleMoves();

        // Parallel processing. Do all moves, keep the one with the highest score.
        List<result> movess = moves.parallelStream().map(move -> {
            // Make a copy of the board so the threads don't share state.
            AbstractGame gameCopy = game.copy();

            // Perform the move, then recursively check possible outcomes.
            gameCopy.performMove(move);

            int score;
            if (gameCopy.getBoard().checkMate(game.nextPlayerColor().opponentColor()))
                score = Integer.MAX_VALUE;
            else
                score = MiniMaxScore(finalDepth - 1, gameCopy, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

            // Undo the move to keep it valid
            gameCopy.undoMove();
            return new result(move, score);
        }).collect(Collectors.toList());
        Optional<result> optionalMove = movess.stream().max(Comparator.comparing(result -> result.score));

        return optionalMove.isPresent() ? optionalMove.get().move : null;
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
     * @param isMaximizing Whether the player should maximize or minimize at
     * this point
     * @param alpha The current alpha value
     * @param beta The current beta value
     *
     * @return The score the board will result in assuming both players play
     * optimally for depth number of moves
     */
    private int MiniMaxScore(int depth, AbstractGame game, boolean isMaximizing, int alpha, int beta) {
        Color maximizerColor = isMaximizing ? game.nextPlayerColor() 
                                            : game.nextPlayerColor().opponentColor();
        
        if (depth <= 0) return game.evaluateScore(maximizerColor);


        List<Move> moves = game.getPossibleMoves();

        if (moves.size() == 0) return game.evaluateScore(maximizerColor);

        // NOTE: code duplication; consider refactoring
        if (isMaximizing) {
            for (Move move : moves) {
                // Make a mistake if difficulty is set to do so.
                if (rand.nextDouble() < chanceOfMistake) continue;

                game.performMove(move);
                // Check if the move puts the opponent in checkmate.
                if (game.getBoard().checkMate(maximizerColor.opponentColor())) {
                    game.undoMove();
                    return 100000 * depth;
                }
                int score = MiniMaxScore(depth - 1, game, false, alpha, beta);
                game.undoMove();

                // Alpha-beta pruning - early return for optimization
                if (score >= beta) return beta;
                alpha = Math.max(alpha, score);
            }
            return alpha;
        }
        else {
            for (Move move : moves) {
                // Make a mistake if difficulty is set to do so.
                if (rand.nextDouble() < chanceOfMistake) continue;

                game.performMove(move);
                // Check if the move puts the opponent in checkmate.
                if (game.getBoard().checkMate(maximizerColor)) {
                    game.undoMove();
                    return -100000 * depth;
                }
                int score = MiniMaxScore(depth - 1, game, true, alpha, beta);
                game.undoMove();

                // Alpha-beta pruning - early return for optimization
                if (score <= alpha) return alpha;
                beta = Math.min(beta, score);
            }
            return beta;
        }
    }
}

// Helper class since Java doesn't have tuples.
class result {
    result(Move m, int s) {
        move = m;
        score = s;
    }
    Move move;
    int score;
}

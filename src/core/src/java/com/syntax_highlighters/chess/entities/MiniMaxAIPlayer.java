package com.syntax_highlighters.chess.entities;
import com.syntax_highlighters.chess.*;

import java.util.List;
import java.util.stream.Collectors;

public class MiniMaxAIPlayer implements IAiPlayer {
    private static final int EASY_DEPTH = 2;
    private static final int MED_DEPTH = 4;
    private static final int HARD_DEPTH = 6;
    private final boolean isWhite;
    private int diff;

    public MiniMaxAIPlayer(boolean isWhite, AiDifficulty diff) {
        this.isWhite = isWhite;
        this.SetDifficulty(diff);
    }

    @Override
    public void PerformMove(Board board) {
        MiniMax.MiniMaxMove(diff, board, isWhite);
    }

    @Override
    public void SetDifficulty(AiDifficulty diff) {
        switch (diff) {
            case Easy: this.diff = EASY_DEPTH; break;
            case Medium: this.diff = MED_DEPTH; break;
            case Hard: this.diff = HARD_DEPTH; break;
            default: throw new IllegalArgumentException("Invalid enum.");
        }
    }
}

class MiniMax {
    public static void MiniMaxMove(int depth, Board board, boolean isWhite) {
        assert(depth >= 1);
        List<Move> moves = board.getAllPieces().stream()
                .filter(p -> p.isWhite() == isWhite)
                .flatMap(p -> p.allPossibleMoves(board).stream())
                .collect(Collectors.toList());

        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        for (Move move : moves) {
            move.DoMove(board);
            int score = MiniMaxScore(depth - 1, board, !isWhite, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            move.UndoMove(board);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        if (bestMove != null) bestMove.DoMove(board);
    }

    private static int MiniMaxScore(int depth, Board board, boolean isWhite, boolean isMaximizing, int alpha, int beta) {
        if (depth <= 0) return evaluateScore(board, isWhite);
        List<Move> moves = board.getAllPieces().stream()
                .filter(p -> p.isWhite() == isWhite)
                .flatMap(p -> p.allPossibleMoves(board).stream())
                .collect(Collectors.toList());

        if (moves.size() == 0) return evaluateScore(board, isWhite);

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (Move move : moves) {
                move.DoMove(board);
                bestScore = Math.max(bestScore, MiniMaxScore(depth - 1, board, isWhite, !isMaximizing, alpha, beta));
                move.UndoMove(board);

                // Alpha-beta pruning - early return for optimization
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) {
                    return bestScore;
                }
            }
            return bestScore;
        }
        else {
            int bestScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                move.DoMove(board);
                bestScore = Math.min(bestScore, MiniMaxScore(depth - 1, board, isWhite, !isMaximizing, alpha, beta));
                move.UndoMove(board);

                // Alpha-beta pruning - early return for optimization
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    return bestScore;
                }
            }
            return bestScore;
        }
    }

    private static int evaluateScore(Board board, boolean forWhite) {
        return board.getAllPieces().stream()
                    .map(p -> getPieceWeight(p, forWhite))
                    .mapToInt(Integer::intValue)
                    .sum();
    }

    private static int getPieceWeight(IChessPiece piece, boolean forWhite) {
        int value = piece.getPieceScore();
        
        // If the piece is not the same colour as the current player,
        // it's weight is negative as we want to get rid of them
        if (forWhite == piece.isWhite()) value = -value;

        return value;
    }
}


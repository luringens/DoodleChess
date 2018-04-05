package com.syntax_highlighters.chess;

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
}

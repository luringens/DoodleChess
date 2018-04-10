package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
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
        IChessPiece queen = new ChessPieceQueen(new Position(3, 3), Color.BLACK);
        pieces.add(queen);
        pieces.add(new ChessPiecePawn(new Position(1, 3), Color.BLACK));
        pieces.add(new ChessPiecePawn(new Position(2, 2), Color.WHITE));

        // Add kings so the AI doesn't think it's Game Over.
        pieces.add(new ChessPieceKing(new Position(8, 8), Color.BLACK));
        pieces.add(new ChessPieceKing(new Position(8, 1), Color.WHITE));

        Board board = new Board(pieces);

        IAiPlayer ai = new MiniMaxAIPlayer(Color.WHITE, AiDifficulty.Hard);

        System.out.println(board);
        ai.PerformMove(board);
        System.out.println(board);

        assertFalse(board.getAllPieces().contains(queen));
    }
}

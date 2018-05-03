package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests pertaining to the behavior of the AI.
 */
class AiTest {
    /**
     * Check that when given the choice of taking a king or taking a pawn,
     * the AI will take the queen.
     */
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
        ChessGame game = ChessGame.setupTestBoard(board, Color.WHITE);
        IAiPlayer ai = new MiniMaxAIPlayer(Color.WHITE, AiDifficulty.ShortSighted);

        System.out.println(board);
        //ai.PerformMove(game);
        System.out.println(board);

        assertFalse(board.getAllPieces().contains(queen));
    }

    /**
     * Check that the AI will spot and perform an obvious checkmate.
     */
    @Test
    void performsObviousCheckmate() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceQueen(new Position(8, 2), Color.BLACK));
        pieces.add(new ChessPiecePawn(new Position(3, 3), Color.BLACK));
        pieces.add(new ChessPieceKing(new Position(1, 1), Color.WHITE));
        pieces.add(new ChessPieceKing(new Position(8, 8), Color.BLACK));
        
        Board board = new Board(pieces);
        ChessGame game = ChessGame.setupTestBoard(board, Color.BLACK);
        IAiPlayer ai = new MiniMaxAIPlayer(Color.BLACK, AiDifficulty.ShortSighted);

        System.out.println(board);
        //ai.PerformMove(game);
        System.out.println(board);

        assertTrue(game.isGameOver());
    }
    @Test
    void ThreatenedQueenShouldMoveAwayTest(){
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        ChessPieceQueen queen = new ChessPieceQueen(new Position(8,1),Color.BLACK);
        pieces.add(queen);
        pieces.add(new ChessPieceQueen(new Position(4,1),Color.WHITE));
        pieces.add(new ChessPieceKing(new Position(5,8),Color.BLACK));
        pieces.add(new ChessPieceRook(new Position(8,2),Color.WHITE));
        pieces.add(new ChessPieceKing(new Position(8,3),Color.WHITE));
        pieces.add(new ChessPieceKnight(new Position(7,1),Color.WHITE));

        Board board = new Board(pieces);
        ChessGame game = ChessGame.setupTestBoard(board,Color.BLACK);
        IAiPlayer ai = new MiniMaxAIPlayer(Color.BLACK,AiDifficulty.ShortSighted);

        System.out.println(board);
        //ai.PerformMove(game);
        System.out.println(board);
        Position queenPos = queen.getPosition() ;

        assertTrue(queenPos != new Position(8,2) || queenPos != new Position(4,1));
    }
}

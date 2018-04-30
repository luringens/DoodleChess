package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.SjadamGame;
import com.syntax_highlighters.chess.entities.IChessPiece;
import com.syntax_highlighters.chess.entities.Color;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class SjadamTest {
    private SjadamGame game;

    @BeforeEach
    public void setUp() {
        game = new SjadamGame();
    }

    @Test
    public void everyPieceHasAMoveFromStartingPositions() {
        List<IChessPiece> pieces = game.getPieces();
        for (IChessPiece p : pieces) {
            assertTrue(game.allPossibleMoves(p).size() > 1,
                    "Every piece should have a move, but " + p.toString() + " does not");
        }
    }
    
    @Test
    public void pieceCanJumpOverOtherPieces() {
        Position kingPos = Position.fromChessNotation("e1"); // white king
        IChessPiece king = game.getPieceAtPosition(kingPos);
        List<Move> kingMoves = game.allPossibleMoves(king);
        
        assertEquals(3, kingMoves.size());
        Position p = kingPos.north(2);
        assertTrue(kingMoves.stream().anyMatch(m -> m.getPosition().equals(p)),
                "King should be able to jump over piece in front");
        
        game.performMove(kingPos, p);
        assertEquals(king, game.getPieceAtPosition(p)); // king should have moved
    }
    
    @Test
    public void playerCannotJumpWithOnePieceAndImmediatelyJumpWithAnother() {
        Position kingPos = Position.fromChessNotation("e1");
        Position pawnInFrontOfKingPos = kingPos.north(1);
        Position newKingPos = kingPos.north(2);

        IChessPiece pawnInFrontOfKing = game.getPieceAtPosition(pawnInFrontOfKingPos);

        game.performMove(kingPos, newKingPos);

        // attempt to jump over king with pawn
        game.performMove(pawnInFrontOfKingPos, pawnInFrontOfKingPos.north(2));

        assertEquals(pawnInFrontOfKing, game.getPieceAtPosition(pawnInFrontOfKingPos));
        assertNull(game.getPieceAtPosition(pawnInFrontOfKingPos.north(2)));
    }

    @Test
    public void onlyPreviouslyMovedPieceCanMoveAfterJumpingHasStarted() {
        Position pos = Position.fromChessNotation("e1");
        IChessPiece piece = game.getPieceAtPosition(pos);
        game.performMove(pos, pos.north(2));
        
        List<Move> possibleMoves = game.allPossibleMoves();
        for (Move m : possibleMoves) {
            assertEquals(piece, m.getPiece(game.getBoard()));
        }
    }

    @Test
    public void turnOfPlayerDoesNotEndAfterJumpingOverOwnPieceOnce() {
        Position kingPos = Position.fromChessNotation("e1");
        Position newPos = kingPos.north(2);
        
        Color playerTurn = game.nextPlayerColor();

        game.performMove(kingPos, newPos);
        assertEquals(playerTurn, game.nextPlayerColor());
    }
    
    @Test
    public void turnOfPlayerDoesNotEndAfterJumpingOverEnemyPieceOnce() {
        // setup
        Position queenPos = Position.fromChessNotation("d1");
        Position rookPos = Position.fromChessNotation("h8");
        Position next = queenPos.north(2);
        Position next2 = next.north(3);
        Position next3 = rookPos.south(2);
        Position next4 = next3.west(3);
            
        game.performMove(queenPos, next);
        game.performMove(next, next2);
        game.performMove(rookPos, next3);
        game.performMove(next3, next4);

        // test the actual thing
        Color color = game.nextPlayerColor();
        game.performMove(next2, next2.east(2)); // try jumping over rook
        assertEquals(color, game.nextPlayerColor());
    }

    @Test
    public void pieceCannotMoveToNonEmptySquare() {
        Position kingPos = Position.fromChessNotation("e1");
        IChessPiece king = game.getPieceAtPosition(kingPos);
        List<Move> kingMoves = game.allPossibleMoves(king);

        // cannot jump over the pieces to the right or to the left
        assertFalse(kingMoves.stream().anyMatch(m -> m.getPosition().equals(kingPos.west(2))));
        assertFalse(kingMoves.stream().anyMatch(m -> m.getPosition().equals(kingPos.east(2))));
    }

    @Test
    public void pieceCannotJumpToOutsideOfTheBoard() {
        Position p = Position.fromChessNotation("g1");
        List<Move> moves = game.allPossibleMoves(game.getPieceAtPosition(p));
        assertFalse(moves.stream().anyMatch(m -> !game.isOnBoard(m.getPosition())));
    }

    @Test
    public void turnEndsAfterPlayerHasJumpedAndThenDoneChessMove() {
        Color color = game.nextPlayerColor();
        Position pos = Position.fromChessNotation("e1");
        Position next = pos.north(2);
        Position next2 = next.north(1);
        
        game.performMove(pos, next);
        game.performMove(next, next2);
        assertNotEquals(color, game.nextPlayerColor());
    }

    @Test
    public void chessPieceCanJumpOverSamePieceWithSameColorTwice() {
        Position pos = Position.fromChessNotation("e1");
        Position next = pos.north(2);

        IChessPiece king = game.getPieceAtPosition(pos);

        game.performMove(pos, next);
        assertEquals(king, game.getPieceAtPosition(next)); // sanity check: piece moved
        
        game.performMove(next, pos); // move piece back
        assertEquals(king, game.getPieceAtPosition(pos)); // piece moved back
    }

    @Test
    public void chessPieceCanJumpOverSamePieceWithDifferentColorOnce() {
        // setup: make queen north until the enemy pawn line, and make pawn jump
        // over the queen
        whiteQueenToD6(); // move queen to d6
        
        Position pawnPos = Position.fromChessNotation("d7");
        Position nextPawnPos = pawnPos.south(2);
        IChessPiece pawn = game.getPieceAtPosition(pawnPos);
        
        //test
        game.performMove(pawnPos, nextPawnPos);
        List<Move> pawnPossibleMoves = game.allPossibleMoves(pawn);

        // ensure pawn cannot move back
        assertFalse(pawnPossibleMoves.stream().anyMatch(m -> m.getPosition().equals(pawnPos)));
    }

    @Test
    public void chessPieceHasOnlyChessMovesAvailableAfterJumpingOverEnemyPiece() {
        whiteQueenToD6(); // move white queen to d6
        
        // move black bishop next to where pawn will land
        // this is just to ensure that there are more options the pawn could
        // have to jump, so that we're testing that pawn cannot jump over other
        // pieces in general, not that it cannot jump back to where it came from
        // in particular
        Position bishopPos = Position.fromChessNotation("f8");
        game.performMove(bishopPos, bishopPos.south(2));
        game.performMove(bishopPos, bishopPos.south(2).southwest(1)); 

        // move white piece to switch turns
        game.performMove(Position.fromChessNotation("a2"), Position.fromChessNotation("a3"));
        
        Position pawnPos = Position.fromChessNotation("d7");
        Position nextPawnPos = pawnPos.south(2);
        IChessPiece pawn = game.getPieceAtPosition(pawnPos);
        
        //test
        game.performMove(pawnPos, nextPawnPos);
        List<Move> pawnPossibleMoves = game.allPossibleMoves(pawn);

        assertEquals(1, pawnPossibleMoves.size()); // should only be one possible move
        assertEquals(nextPawnPos.south(1), pawnPossibleMoves.get(0)); // which is a step forward

    }

    @Test
    public void pieceCanPerformChessMoveWithoutJumpingFirst() {
        Position a2 = Position.fromChessNotation("a2");
        Position a3 = Position.fromChessNotation("a3");

        IChessPiece pawn = game.getPieceAtPosition(a2);
        
        // test that move is performed
        game.performMove(a2, a3);
        assertEquals(pawn, game.getPieceAtPosition(a3));
    }

    // sorry about the long test names
    @Test
    public void playerCanMoveDifferentPieceIfThePiecePreviouslyMovedIsReplacedInItsStartingPosition() {
        // NOTE: This replicates the behavior found in the sjadam game
        // implemented at https://sjadam.no (so it's not just something random I
        // made up to create problems, although it might feel like it)
        // Whether this is the desired behavior or not is up for debate, but
        // Jonathan did say we could consider the behavior at sjadam.no the
        // canonical one.

        List<Move> possibleMoves = game.allPossibleMoves(); // all possible moves from starting positions
        
        Position pos = Position.fromChessNotation("e5");
        Position next = pos.north(2);
        game.performMove(pos, next);
        game.performMove(next, pos); // move piece back
        
        assertEquals(possibleMoves.size(), game.allPossibleMoves().size()); // all moves should still be possible
    }

    /**
     * Helper function: from starting positions, move white queen from d1 to d6
     * by jumping over the pawn at d2 and then moving forward to d6.
     *
     * Modifies the game board state.
     */
    private void whiteQueenToD6() {
        Position pos = Position.fromChessNotation("d1");
        Position next = pos.north(2);   // d3
        Position next2 = next.north(3); // d6
        game.performMove(pos, next);
        game.performMove(next, next2);
    }
}

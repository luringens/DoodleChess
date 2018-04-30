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

/**
 * Test that the SjadamGame class behaves as expected when the player attempts
 * to particular things relating to the rules in sjadam.
 */
public class SjadamTest {
    private SjadamGame game; // a game initialized to starting positions

    @BeforeEach
    public void setUp() {
        // setup a new game
        game = new SjadamGame();
    }

    @Test
    public void everyPieceHasAMoveFromStartingPositions() {
        // check that every piece has a move from the starting positions (note
        // that not all of the moves are sjadam moves, but the pieces in the
        // back, most of which cannot move from the starting positions in
        // regular chess, can each jump over at least one other piece with
        // sjadam rules)
        List<IChessPiece> pieces = game.getPieces();
        for (IChessPiece p : pieces) {
            // check that the number of possible moves is at least 1
            assertTrue(game.allPossibleMoves(p).size() >= 1,
                    "Every piece should have a move, but " + p.toString() + " does not");
        }
    }
    
    @Test
    public void pieceCanJumpOverOtherPieces() {
        // check that the king has a move that that takes it over the pawn in
        // front of it, and that performing this move works as expected
        Position kingPos = Position.fromChessNotation("e1"); // white king
        IChessPiece king = game.getPieceAtPosition(kingPos);
        List<Move> kingMoves = game.allPossibleMoves(king);
        
        // just a sanity check: should be able to jump over three different
        // pawns
        assertEquals(3, kingMoves.size());
        
        // ensure that there is a move that goes over the pawn in front of the
        // king
        Position p = kingPos.north(2);
        assertTrue(kingMoves.stream().anyMatch(m -> m.getPosition().equals(p)),
                "King should be able to jump over piece in front");
        
        game.performMove(kingPos, p); // actually perform the move
        assertEquals(king, game.getPieceAtPosition(p)); // king should have moved
    }
    
    @Test
    public void playerCannotJumpWithOnePieceAndImmediatelyJumpWithAnother() {
        // let the king jump over the pawn, then try to make the pawn jump over
        // the king that's now in front of it, and make sure that the game
        // doesn't allow this
        Position kingPos = Position.fromChessNotation("e1");
        Position pawnInFrontOfKingPos = kingPos.north(1);
        Position newKingPos = kingPos.north(2);

        IChessPiece pawnInFrontOfKing = game.getPieceAtPosition(pawnInFrontOfKingPos);

        game.performMove(kingPos, newKingPos);

        // attempt to jump over king with pawn
        game.performMove(pawnInFrontOfKingPos, pawnInFrontOfKingPos.north(2));

        // ensure that the pawn didn't move
        assertEquals(pawnInFrontOfKing, game.getPieceAtPosition(pawnInFrontOfKingPos));
        assertNull(game.getPieceAtPosition(pawnInFrontOfKingPos.north(2)));
    }

    @Test
    public void onlyPreviouslyMovedPieceCanMoveAfterJumpingHasStarted() {
        // try to jump with the king, and check that after the move has been
        // performed, the only possible moves in the game are moves which use
        // the king piece
        Position pos = Position.fromChessNotation("e1");
        IChessPiece piece = game.getPieceAtPosition(pos);
        
        game.performMove(pos, pos.north(2)); // perform the jump
        
        // check that for every possible move, the king is the piece that moves
        List<Move> possibleMoves = game.allPossibleMoves();
        for (Move m : possibleMoves) {
            assertEquals(piece, m.getPiece(game.getBoard()));
        }
    }

    @Test
    public void turnOfPlayerDoesNotEndAfterJumpingOverOwnPieceOnce() {
        // try to jump with the king, and ensure that it's still the same
        // player's turn after the jump
        Position kingPos = Position.fromChessNotation("e1");
        Position newPos = kingPos.north(2);
        
        Color playerTurn = game.nextPlayerColor(); // store player color

        game.performMove(kingPos, newPos);
        
        // ensure that player color didn't change
        assertEquals(playerTurn, game.nextPlayerColor());
    }
    
    @Test
    public void turnOfPlayerDoesNotEndAfterJumpingOverEnemyPieceOnce() {
        // setup: move queen to d6, and the black rook from h8 to e6 by jumping
        // over the pawn in front of it and then making a regular chess move to
        // bring it up next to the queen. Then try to jump over the rook before
        // checking that the turn is not over.
        whiteQueenToD6(); // move the queen

        // move the black rook from h8 to e6 in a two-step process
        Position rookPos = Position.fromChessNotation("h8");
        Position rookNext = rookPos.south(2);
        Position rookNext2 = rookNext.west(3);
            
        game.performMove(rookPos, rookNext);   // to h6
        game.performMove(rookNext, rookNext2); // to e6

        // test the actual thing
        Color color = game.nextPlayerColor();
        game.performMove(rookNext2.west(1), rookNext2.east(1)); // try jumping over rook
        // ensure current player color didn't change
        assertEquals(color, game.nextPlayerColor());
    }

    @Test
    public void pieceCannotMoveToNonEmptySquare() {
        // check that the king cannot jump over the queen or the right bishop
        // from its starting position
        Position kingPos = Position.fromChessNotation("e1");
        IChessPiece king = game.getPieceAtPosition(kingPos);
        
        List<Move> kingMoves = game.allPossibleMoves(king);

        // ensure that the king cannot move to either the position two spaces to
        // its right or two spaces to its left
        assertFalse(kingMoves.stream().anyMatch(m -> m.getPosition().equals(kingPos.west(2))));
        assertFalse(kingMoves.stream().anyMatch(m -> m.getPosition().equals(kingPos.east(2))));
    }

    @Test
    public void pieceCannotJumpToOutsideOfTheBoard() {
        // the knight at g1 could jump over the rook to its right if the
        // position on the other side was on the board, but since it isn't, it
        // shouldn't be in the possible moves
        Position p = Position.fromChessNotation("g1");
        
        List<Move> moves = game.allPossibleMoves(game.getPieceAtPosition(p));
        
        // ensure there is no move such that the target position is outside the
        // board
        assertFalse(moves.stream().anyMatch(m -> !game.isOnBoard(m.getPosition())));
    }

    @Test
    public void turnEndsAfterPlayerHasJumpedAndThenDoneChessMove() {
        // store color to check later
        Color color = game.nextPlayerColor();
        
        // define positions involved in the two moves
        Position pos = Position.fromChessNotation("e1");
        Position next = pos.north(2);
        Position next2 = next.north(1);
        
        // perform moves
        game.performMove(pos, next);
        game.performMove(next, next2);
        
        // ensure that the color of current player changed
        assertNotEquals(color, game.nextPlayerColor());
    }

    @Test
    public void chessPieceCanJumpOverSamePieceWithSameColorTwice() {
        Position pos = Position.fromChessNotation("e1");
        Position next = pos.north(2);

        IChessPiece king = game.getPieceAtPosition(pos);

        // jump over a piece with the king
        game.performMove(pos, next);
        assertEquals(king, game.getPieceAtPosition(next)); // sanity check: king moved
        
        game.performMove(next, pos); // move king back
        assertEquals(king, game.getPieceAtPosition(pos)); // ensure that king moved back
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
        // setup: make queen north until the enemy pawn line, move black bishop
        // to the position southeast of the queen, and make the black pawn jump
        // over the queen. The pawn should then only be allowed to make a chess
        // move, which will be a single step forward
        whiteQueenToD6(); // move white queen to d6
        
        // move black bishop next to where pawn will land
        // this is just to ensure that there are more options the pawn could
        // have to jump, so that we're testing that pawn cannot jump over other
        // pieces in general, not that it cannot jump back to where it came from
        // in particular
        Position bishopPos = Position.fromChessNotation("f8");
        game.performMove(bishopPos, bishopPos.south(2));              // to f6
        game.performMove(bishopPos, bishopPos.south(2).southwest(1)); // to e5

        // move white pawn to switch turns
        game.performMove(Position.fromChessNotation("a2"), Position.fromChessNotation("a3"));
        
        Position pawnPos = Position.fromChessNotation("d7");
        Position nextPawnPos = pawnPos.south(2);
        IChessPiece pawn = game.getPieceAtPosition(pawnPos);
        
        // perform the pawn move
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
        game.performMove(pos, next); // jump over the pawn in front
        game.performMove(next, pos); // jump back
        
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

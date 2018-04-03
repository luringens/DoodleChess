package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tests designed to check some of the weirder rules of chess, such as en
 * passant and castling.
 */
public class ChessRulesTest {
    private Board board;
    private List<IChessPiece> pieces;
    private ChessPiecePawn whitePawn;
    private ChessPiecePawn blackPawn;
    private ChessPieceKing king;
    private ChessPieceRook rook;
    
    /**
     * Helper method: initialize board like in a normal chess game.
     */
    public void setUpStandard() {
        board = new Board();
        board.setupNewGame();
        pieces = board.getAllPieces();
    }

    /**
     * Helper method: initialize board in such a way that castling between left
     * rook and king for white player is possible.
     *
     * Only set up those two pieces in the correct positions. They can
     * henceforth be accessed using the variables "king" and "rook".
     */
    public void setUpCastle() {
        king = new ChessPieceKing(new Position(5, 1), true);
        rook = new ChessPieceRook(new Position(1, 1), true);
        
        pieces = new ArrayList<>();
        pieces.add(king);
        pieces.add(rook);
        board = new Board(pieces);
    }

    /**
     * Helper method: initialize board with one white pawn at position D2 and
     * one black pawn at position E7.
     *
     * Only set up those two pieces in the correct positions. They can
     * henceforth be accessed using the variables "whitePawn" and "blackPawn".
     */
    @Before
    public void setUp() {
        whitePawn = new ChessPiecePawn(new Position(4, 2), true);
        blackPawn = new ChessPiecePawn(new Position(5, 7), false);
        
        pieces = new ArrayList<>();
        pieces.add(whitePawn);
        pieces.add(blackPawn);
        
        board = new Board(pieces);
    }
    
    @Test
    public void whitePawnCanMoveTwoStepsOnce() {
        setUp();
        Position twoForward = forward(whitePawn, 2);

        existsMoveToPosition(twoForward, whitePawn);
    }
    
    @Test
    public void blackPawnCanMoveTwoStepsOnce() {
        setUp();
        Position twoForward = forward(blackPawn, 2);

        existsMoveToPosition(twoForward, blackPawn);
    }
    
    @Test
    public void whitePawnCannotMoveTwoStepsTwice() {
        setUp();
        
        // move forward two steps
        Position twoForward = forward(whitePawn, 2);
        board.movePiece(whitePawn, twoForward);
        
        // get new "two forward" position
        twoForward = forward(whitePawn, 2);
        noAvailableMoveLeadsTo(twoForward, whitePawn);
    }
    
    @Test
    public void blackPawnCannotMoveTwoStepsTwice() {
        setUp();

        // move forward two steps
        Position twoForward = forward(blackPawn, 2);
        board.movePiece(blackPawn, twoForward);
        
        // get new "two forward" position
        twoForward = forward(blackPawn, 2);
        noAvailableMoveLeadsTo(twoForward, blackPawn);
    }

    @Test
    public void whitePawnCanPerformEnPassant() {
        setUp();
        
        // setup board:
        //  A B C D E F G H 
        // _________________
        // |               | 8
        // |        .      | 7 . = old position of black pawn
        // |        x      | 6 x = target
        // |      W B      | 5 W = white pawn, B = black pawn
        // |               | 4
        // |               | 3
        // |               | 2
        // |_______________| 1
        
        board.movePiece(whitePawn, forward(whitePawn, 2)); // move to rank 4
        board.movePiece(whitePawn, forward(whitePawn, 1)); // move to rank 5
        
        board.movePiece(blackPawn, forward(blackPawn, 2)); // move to rank 5

        Position blackPos = blackPawn.getPosition();
        
        // last performed move should be the move of blackPawn, so whitePawn
        // should be able to perform en passant in this situation
        Position whiteTarget = whitePawn.getPosition().northeast(1);
        existsMoveToPosition(whiteTarget, whitePawn);

        // check that black pawn gets captured
        board.movePiece(whitePawn, whiteTarget);
        assertTrue("En passant does not capture enemy piece",
                board.getAtPosition(blackPos) == null);
    }
    
    @Test
    public void blackPawnCanPerformEnPassant() {
        setUp(); // I really need to figure out that @Before bug...

        // setup board:
        //  A B C D E F G H 
        // _________________
        // |               | 8
        // |               | 7 
        // |               | 6
        // |               | 5
        // |      W B      | 4 W = white pawn, B = black pawn
        // |      x        | 3 x = target
        // |      .        | 2 . = old position of black pawn
        // |_______________| 1

        board.movePiece(blackPawn, forward(blackPawn, 2)); // move to rank 5
        board.movePiece(blackPawn, forward(blackPawn, 1)); // move to rank 4

        board.movePiece(whitePawn, forward(whitePawn, 2)); // move to rank 4

        Position whitePos = whitePawn.getPosition();

        // Execute en passant
        Optional<Move> enpassant = blackPawn.allPossibleMoves(board).stream()
                .filter(m -> m instanceof  EnPassantMove)
                .findFirst();
        assertTrue("No en passant move available", enpassant.isPresent());
        enpassant.get().DoMove(board);

        // check that white pawn gets captured
        assertTrue("En passant does not capture enemy piece",
                board.getAtPosition(whitePos) == null);
    }

    @Test
    public void pawnCannotPerformEnPassantIfOtherPawnNotJustMoved() {
        // tests just one kind of pawn, since presumably this works the same
        // for either color
        setUp();
        
        board.movePiece(whitePawn, forward(whitePawn, 2));
        board.movePiece(blackPawn, forward(blackPawn, 2));
        board.movePiece(whitePawn, forward(whitePawn, 1));

        // white pawn is now in position to perform en passant, but black pawn
        // didn't recently move
        noAvailableMoveLeadsTo(whitePawn.getPosition().northeast(1), whitePawn);
    }

    @Test
    public void pawnCannotPerformEnPassantIfLastMoveWasSingleStep() {
        // tests just one kind of pawn, since presumably this works the same
        // for either color
        setUp();
        
        board.movePiece(whitePawn, forward(whitePawn, 2));
        board.movePiece(blackPawn, forward(blackPawn, 1));
        board.movePiece(whitePawn, forward(whitePawn, 1));
        board.movePiece(blackPawn, forward(blackPawn, 1));

        // white pawn is now in position to perform en passant, but black pawn
        // moved last with a single step, not a double step
        noAvailableMoveLeadsTo(whitePawn.getPosition().northeast(1), whitePawn);
    }

    @Test
    public void knightsCanJumpOverPieces() {
        setUpStandard(); // set up a standard game of chess

        List<IChessPiece> knights = pieces.stream()
            .filter(p -> p instanceof ChessPieceKnight)
            .collect(Collectors.toList());

        assertEquals(4, knights.size()); // sanity check
        for (IChessPiece k : knights) {
            // assert that the knight is blocked in all directions
            for (Position n : getNeighbors(k.getPosition())) {
                assertTrue(board.isOccupied(n));
            }

            // assert that the knight still has two moves
            List<Move> moves = k.allPossibleMoves(board);
            assertEquals(2, moves.size());

            // assert that the positions moved to are unoccupied
            for (Move m : moves) {
                Position p = m.getPosition();
                assertTrue(board.isOnBoard(p));
                assertFalse(board.isOccupied(p));
            }
        }
    }

    @Test
    public void kingCanPerformCastleWithUnmovedTower() {
        setUpCastle();
        Position oldKingPos = king.getPosition();
        Position kingTarget = oldKingPos.west(2);
        
        List<Move> possibleMoves = king.allPossibleMoves(board);

        existsMoveToPosition(kingTarget, king);
        
        // check that both pieces actually move
        board.movePiece(king, kingTarget);
        
        // check that the king moves
        assertEquals(kingTarget, king.getPosition());
        assertEquals(king, board.getAtPosition(kingTarget));
        
        // check that the rook moves
        assertEquals(kingTarget.east(1), rook.getPosition());
        assertEquals(rook, board.getAtPosition(kingTarget.east(1)));
    }

    @Test
    public void kingCannotPerformCastleWithMovedTower() {
        setUpCastle();

        board.movePiece(rook, rook.getPosition().north(1));
        board.movePiece(rook, rook.getPosition().south(1));
        
        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
    }
    
    @Test
    public void kingCannotPerformCastleAfterMoved() {
        setUpCastle();

        board.movePiece(king, king.getPosition().north(1));
        board.movePiece(king, king.getPosition().south(1));
        
        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
    }

    @Test
    public void kingCannotPerformCastleWhenPathThreatened() {
        setUpCastle();
        Position rookpos = new Position(4, 8);
        board.putAtPosition(rookpos, new ChessPieceRook(rookpos, false));
        
        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
        
    }

    @Test
    public void kingCannotPerformCastleWhenPathBlocked() {
        setUpStandard(); // standard chess game - there are pieces to either side of the kings
        List<IChessPiece> kings = pieces.stream()
            .filter(p -> p instanceof ChessPieceKing)
            .collect(Collectors.toList());

        for (IChessPiece piece : kings) {
            noAvailableMoveLeadsTo(piece.getPosition().west(2), piece);
            noAvailableMoveLeadsTo(piece.getPosition().east(2), piece);
        }
    }

    @Test
    public void kingCannotPerformCastleWithEnemyPiece() {
        setUpCastle();
        // replace the white rook with a black rook
        Position rookpos = new Position(1, 1);
        rook = new ChessPieceRook(rookpos, false);
        board.putAtPosition(rookpos, rook);

        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
    }

    @Test
    public void kingCannotMoveToThreatenedPosition() {
        setUpCastle(); // so we have a convenient way of referring to the king
        Position rookpos = new Position(4, 8); // threaten position 4,1
        board.putAtPosition(rookpos, new ChessPieceRook(rookpos, false));

        Position threatenedPos1 = king.getPosition().west(1);
        Position threatenedPos2 = king.getPosition().northwest(1);

        noAvailableMoveLeadsTo(threatenedPos1, king);
        noAvailableMoveLeadsTo(threatenedPos2, king);
    }
    
    /**
     * Helper method: check that no available move of the given piece leads to
     * the given position.
     *
     * Uses assert
     *
     * @param target The position to check for
     * @param piece The piece to be moved
     */
    public void noAvailableMoveLeadsTo(Position target, IChessPiece piece) {
        List<Move> possibleMoves = piece.allPossibleMoves(board);

        for (Move m : possibleMoves) {
            assertNotEquals(target, m.getPosition());
        }
    }

    /**
     * Helper method: check that there is exactly one move available to the
     * piecepiece leading to the given position.
     *
     * Uses assert
     *
     * @param target The position to check for
     * @param piece The piece to be moved
     */
    public void existsMoveToPosition(Position target, IChessPiece piece) {
        assertTrue("No move to " + target, piece.canMoveTo(target, board));
    }
    
    /**
     * Helper method: get neighbors of position.
     *
     * Limited to the neighbors which are on the board.
     *
     * @param pos The position to get the neighboring positions of
     * @return A list of the neighbors to the given position
     */
    private List<Position> getNeighbors(Position pos) {
        return pos.neighbors().stream()
            .filter(p -> board.isOnBoard(p))
            .collect(Collectors.toList());
    }

    /**
     * Helper method: make pawn go forward n steps, regardless of color.
     *
     * @return The position n steps forward, as per color of the pawn
     */
    private Position forward(ChessPiecePawn pawn, int nSteps) {
        if (pawn.isWhite())
            return pawn.getPosition().north(nSteps);
        return pawn.getPosition().south(nSteps);
    }
}

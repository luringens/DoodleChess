package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tests designed to check some of the weirder rules of chess, such as en
 * passant and castling.
 */
class ChessRulesTest {
    private Board board;
    private List<IChessPiece> pieces;
    private ChessPiecePawn whitePawn;
    private ChessPiecePawn blackPawn;
    private ChessPieceKing king;
    private ChessPieceRook rook;
    
    /**
     * Helper method: initialize board like in a normal chess game.
     */
    private void setUpStandard() {
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
    private void setUpCastle() {
        king = new ChessPieceKing(new Position(5, 1), Color.WHITE);
        rook = new ChessPieceRook(new Position(1, 1), Color.WHITE);
        
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
    @BeforeEach
    private void setUp() {
        whitePawn = new ChessPiecePawn(new Position(4, 2), Color.WHITE);
        blackPawn = new ChessPiecePawn(new Position(5, 7), Color.BLACK);
        
        pieces = new ArrayList<>();
        pieces.add(whitePawn);
        pieces.add(blackPawn);
        
        board = new Board(pieces);
    }
    
    @Test
    void whitePawnCanMoveTwoStepsOnce() {
        setUp();
        Position twoForward = forward(whitePawn, 2);

        existsMoveToPosition(twoForward, whitePawn);
    }
    
    @Test
    void blackPawnCanMoveTwoStepsOnce() {
        setUp();
        Position twoForward = forward(blackPawn, 2);

        existsMoveToPosition(twoForward, blackPawn);
    }
    
    @Test
    void whitePawnCannotMoveTwoStepsTwice() {
        setUp();
        
        // move forward two steps
        Position twoForward = forward(whitePawn, 2);
        board.movePiece(whitePawn, twoForward);
        
        // get new "two forward" position
        twoForward = forward(whitePawn, 2);
        noAvailableMoveLeadsTo(twoForward, whitePawn);
    }

    @Test
    void blackPawnCannotMoveTwoStepsTwice() {
        setUp();

        // move forward two steps
        Position twoForward = forward(blackPawn, 2);
        board.movePiece(blackPawn, twoForward);

        // get new "two forward" position
        twoForward = forward(blackPawn, 2);
        noAvailableMoveLeadsTo(twoForward, blackPawn);
    }

    @Test
    void whitePawnCanPerformEnPassant() {
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
        assertTrue(board.getAtPosition(blackPos) == null, "En passant does not capture enemy piece");
    }

    @Test
    void noAvailableMovesMeansGameOver() {
        //  A B C D E F G H
        // _________________
        // |. . . . . . . .| 8
        // |. . . . . K . .| 7 White king
        // |. . . . . . p K| 6 White pawn, black king
        // |. . . . . . . p| 5 Black pawn
        // |. . . . . . . p| 4 White pawn
        // |. . . . . . . .| 3
        // |. . . . . . . .| 2
        // |._._._._._._._.| 1

        List<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceKing(new Position(6, 7), Color.WHITE));
        pieces.add(new ChessPiecePawn(new Position(7, 6), Color.WHITE));
        pieces.add(new ChessPieceKing(new Position(8, 6), Color.BLACK));
        pieces.add(new ChessPiecePawn(new Position(8, 5), Color.BLACK));
        pieces.add(new ChessPiecePawn(new Position(8, 4), Color.WHITE));
        Board b = new Board(pieces);
        ChessGame g = ChessGame.setupTestBoard(b, Color.BLACK);
        System.out.println(b);
        assertTrue(g.isGameOver());
    }

    @Test
    void blackPawnCanPerformEnPassant() {
        setUp();
        
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
                .filter(m -> m instanceof EnPassantMove)
                .findFirst();
        assertTrue(enpassant.isPresent(), "No en passant move available");
        enpassant.get().DoMove(board);

        // check that white pawn gets captured
        assertTrue(board.getAtPosition(whitePos) == null, "En passant does not capture enemy piece");
    }

    @Test
    void pawnCannotPerformEnPassantIfOtherPawnNotJustMoved() {
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
    void pawnCannotPerformEnPassantIfLastMoveWasSingleStep() {
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
    void knightsCanJumpOverPieces() {
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
    void kingCanPerformCastleWithUnmovedTower() {
        setUpCastle();
        Position oldKingPos = king.getPosition();
        Position kingTarget = oldKingPos.west(2);

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
    void kingCannotPerformCastleWithMovedTower() {
        setUpCastle();

        board.movePiece(rook, rook.getPosition().north(1));
        board.movePiece(rook, rook.getPosition().south(1));
        
        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
    }
    
    @Test
    void kingCannotPerformCastleAfterMoved() {
        setUpCastle();

        board.movePiece(king, king.getPosition().north(1));
        board.movePiece(king, king.getPosition().south(1));
        
        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
    }

    @Test
    void kingCannotPerformCastleWhenPathThreatened() {
        setUpCastle();
        Position rookpos = new Position(4, 8);
        board.putAtPosition(rookpos, new ChessPieceRook(rookpos, Color.BLACK));
        
        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
        
    }

    @Test
    void kingCannotPerformCastleWhenPathBlocked() {
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
    void kingCannotPerformCastleWithEnemyPiece() {
        setUpCastle();
        // replace the white rook with a black rook
        Position rookpos = new Position(1, 1);
        rook = new ChessPieceRook(rookpos, Color.BLACK);
        board.putAtPosition(rookpos, rook);

        noAvailableMoveLeadsTo(king.getPosition().west(2), king);
    }

    @Test
    void kingCannotMoveToThreatenedPosition() {
        setUpCastle(); // so we have a convenient way of referring to the king
        Position rookpos = new Position(4, 8); // threaten position 4,1
        board.putAtPosition(rookpos, new ChessPieceRook(rookpos, Color.BLACK));

        Position threatenedPos1 = king.getPosition().west(1);
        Position threatenedPos2 = king.getPosition().northwest(1);

        noAvailableMoveLeadsTo(threatenedPos1, king);
        noAvailableMoveLeadsTo(threatenedPos2, king);
    }

    @Test
    void cantEndTurnInCheck() {
        setUp();

        //  A B C D E F G H
        // _________________
        // |. . . . . . . .| 8
        // |. . . . K . . .| 7 Black king
        // |. . . . . . . .| 6
        // |. R . . . . . .| 5 Black rook
        // |. . . . . . . .| 4
        // |. . . . . . . .| 3
        // |. . . R R R . .| 2 White rooks
        // |._._._._._._._K| 1 White king

        // In this situation, the only valid move for black should be to
        // move the rook to block the enemy rook from taking the king.

        List<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceKing(new Position(5, 7), Color.BLACK));
        pieces.add(new ChessPieceRook(new Position(2, 5), Color.BLACK));
        pieces.add(new ChessPieceRook(new Position(4, 2), Color.WHITE));
        pieces.add(new ChessPieceRook(new Position(5, 2), Color.WHITE));
        pieces.add(new ChessPieceRook(new Position(6, 2), Color.WHITE));
        pieces.add(new ChessPieceKing(new Position(8, 1), Color.WHITE));
        Board b = new Board(pieces);
        System.out.println(b);

        long validMoves = b.getAllPieces()
                           .stream()
                           .filter(p -> p.getColor().isBlack())
                           .mapToLong(p -> p.allPossibleMoves(b).size())
                           .sum();

        assertEquals(1, validMoves);
    }
    @Test
    void OnlyTwoKingsLeftTest(){
        List<IChessPiece> pieces = new ArrayList<>();
        pieces.add(new ChessPieceKing(new Position(5,1),Color.WHITE));
        pieces.add(new ChessPieceKing(new Position(5,8),Color.BLACK));
        Board b = new Board(pieces);
        ChessGame game = ChessGame.setupTestBoard(board, Color.WHITE);

        assertTrue(game.isGameOver());
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
    private void noAvailableMoveLeadsTo(Position target, IChessPiece piece) {
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
    private void existsMoveToPosition(Position target, IChessPiece piece) {
        assertTrue(piece.canMoveTo(target, board), "No move to " + target);
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
        if (pawn.getColor().isWhite())
            return pawn.getPosition().north(nSteps);
        return pawn.getPosition().south(nSteps);
    }


}

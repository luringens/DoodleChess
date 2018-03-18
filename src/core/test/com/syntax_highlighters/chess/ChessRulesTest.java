package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessRulesTest {
    private Board board;
    private List<IChessPiece> pieces;
    private ChessPiecePawn whitePawn;
    private ChessPiecePawn blackPawn;
    private ChessPieceKing king;
    private ChessPieceRook rook;
    
    public void setUpStandard() {
        board = new Board();
        board.setupNewGame();
        pieces = board.getAllPieces();
    }

    public void setUpCastle() {
        king = new ChessPieceKing(new Position(5, 1), true);
        rook = new ChessPieceRook(new Position(1, 1), true);
        
        pieces = new ArrayList<>();
        pieces.add(king);
        pieces.add(rook);
        board = new Board(pieces);
    }

    // this does not work correctly...
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
        Position twoForward = whitePawn.getPosition().north(2);

        List<Move> possibleMoves = whitePawn.allPossibleMoves(board);
        assertEquals(1, possibleMoves.stream()
                                     .map(p -> p.getPosition())
                                     .filter(p -> p.equals(twoForward))
                                     .collect(Collectors.toList())
                                     .size());
    }
    
    @Test
    public void blackPawnCanMoveTwoStepsOnce() {
        setUp();
        Position twoForward = blackPawn.getPosition().south(2);

        List<Move> possibleMoves = blackPawn.allPossibleMoves(board);
        assertEquals(1, possibleMoves.stream()
                                     .map(p -> p.getPosition())
                                     .filter(p -> p.equals(twoForward))
                                     .collect(Collectors.toList())
                                     .size());
    }
    
    @Test
    public void whitePawnCannotMoveTwoStepsTwice() {
        setUp();
        
        // move forward two steps
        Position twoForward = whitePawn.getPosition().north(2);
        board.movePiece(whitePawn, twoForward);
        
        // get new "two forward" position
        twoForward = whitePawn.getPosition().north(2);
        
        List<Move> moves = whitePawn.allPossibleMoves(board);
        for (Move move : moves) {
            assertFalse(move.getPosition().equals(twoForward));
        }
    }
    
    @Test
    public void blackPawnCannotMoveTwoStepsTwice() {
        setUp();

        // move forward two steps
        Position twoForward = blackPawn.getPosition().south(2);
        board.movePiece(blackPawn, twoForward);
        
        // get new "two forward" position
        twoForward = blackPawn.getPosition().south(2);
        
        List<Move> moves = blackPawn.allPossibleMoves(board);
        for (Move move : moves) {
            assertFalse(move.getPosition().equals(twoForward));
        }
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
        
        board.movePiece(whitePawn, whitePawn.getPosition().north(2)); // move to rank 4
        board.movePiece(whitePawn, whitePawn.getPosition().north(1)); // move to rank 5
        
        board.movePiece(blackPawn, blackPawn.getPosition().south(2)); // move to rank 5
        
        // last performed move should be the move of blackPawn, so whitePawn
        // should be able to perform en passant in this situation
        Position whiteTarget = whitePawn.getPosition().northeast(1);
        List<Move> possibleMoves = whitePawn.allPossibleMoves(board);
        assertEquals(1, possibleMoves.stream()
                                     .map(p -> p.getPosition())
                                     .filter(p -> p.equals(whiteTarget))
                                     .collect(Collectors.toList())
                                     .size());
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

        board.movePiece(blackPawn, blackPawn.getPosition().south(2)); // move to rank 5
        board.movePiece(blackPawn, blackPawn.getPosition().south(1)); // move to rank 4

        board.movePiece(whitePawn, whitePawn.getPosition().north(2)); // move to rank 4

        Position blackTarget = blackPawn.getPosition().southwest(1);
        List<Move> possibleMoves = blackPawn.allPossibleMoves(board);
        assertEquals(1, possibleMoves.stream()
                                     .map(p -> p.getPosition())
                                     .filter(p -> p.equals(blackTarget))
                                     .collect(Collectors.toList())
                                     .size());
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
        assertEquals(1, possibleMoves.stream()
                .map(p -> p.getPosition())
                .filter(p -> p.equals(kingTarget))
                .collect(Collectors.toList())
                .size());

        // check that both pieces actually move
        board.movePiece(king, kingTarget);
        assertEquals(kingTarget, king.getPosition());
        assertEquals(kingTarget.east(1), rook.getPosition());
    }

    List<Position> getNeighbors(Position pos) {
        return Arrays.asList(new Position[] {
                pos.north(1), pos.south(1),
                pos.east(1), pos.west(1),
                pos.northeast(1), pos.southeast(1),
                pos.northwest(1), pos.southwest(1)}).stream()
            .filter(p -> board.isOnBoard(p))
            .collect(Collectors.toList());
    }



    // TODO add test kingCanPerformCastleWithUnmovedTower
    // TODO add test kingCannotPerformCastleWithMovedTower
    // TODO add test kingCannotPerformCastleAfterMoved
    // TODO add test kingCannotPerformCastleWhenPathThreatened
    // TODO add test kingCannotPerformCastleWhenPathBlocked
}

package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessRulesTest {
    private Board board;
    private List<IChessPiece> pieces;
    private ChessPiecePawn whitePawn;
    private ChessPiecePawn blackPawn;

    // this does not work correctly...
    @Before
    public void setUp() {
        whitePawn = new ChessPiecePawn(new Position(4, 2), true);
        blackPawn = new ChessPiecePawn(new Position(4, 7), false);
        
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

    // TODO add test blackPawnCanPerformEnPassant
    // TODO add test whitePawnCanPerformEnPassant
    // TODO add test knightsCanJumpOverPieces
    // TODO add test kingCanPerformCastleWithUnmovedTower
    // TODO add test kingCannotPerformCastleWithMovedTower
    // TODO add test kingCannotPerformCastleAfterMoved
    // TODO add test kingCannotPerformCastleWhenPathThreatened
}

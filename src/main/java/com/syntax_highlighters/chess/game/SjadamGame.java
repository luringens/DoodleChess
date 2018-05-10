package com.syntax_highlighters.chess.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKing;
import com.syntax_highlighters.chess.chesspiece.ChessPieceQueen;
import com.syntax_highlighters.chess.Color;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;
import com.syntax_highlighters.chess.move.Move;
import com.syntax_highlighters.chess.move.PromotionMove;

/**
 * Custom game mode with sjadam rules.
 *
 * https://github.com/JonasTriki/sjadam-js
 * www.sjadam.no
 */
public class SjadamGame extends AbstractGame {
    private List<Position> jumpedFromPositions = new ArrayList<>();

    public SjadamGame() {
        this.board = new Board();
        this.board.setupNewGame();
    }

    private boolean lastMoveWasSameColour(){
        Move m = board.getLastMove();
        if (m == null) return false;
        Color col = m.getColor(board);
        return col == nextPlayerColor;
    }

    private IChessPiece lastPiece(){
        return board.getAtPosition(board.getLastMove().getPosition());
    }

    private boolean lastJumpedPieceWasEnemy() {
        if (getLastJumpedPosition() == null) return false;
        Move m = board.getLastMove();
        Position mid = m.getOldPosition().stepsToPosition(m.getPosition()).get(1);
        return board.getAtPosition(mid).getColor() == nextPlayerColor.opponentColor();
    }
    
    private Position getLastJumpedPosition() {
        if (jumpedFromPositions.size() == 0) return null;
        return jumpedFromPositions.get(jumpedFromPositions.size()-1);
    }


    /**
     * Get a list of all the possible moves that can be made during this turn.
     *
     * Takes care of any restrictions put in place due to stateful changes in
     * the SjadamGame.
     *
     * @return A list of all the possible moves that can be made by the current
     * player
     */
    @Override
    public List<Move> allPossibleMoves() {
        List<IChessPiece> pieces = getPieces().stream()
            .filter(p -> p.getColor() == nextPlayerColor())
            .filter(p -> getLastJumpedPosition() == null || p == lastPiece())
            .collect(Collectors.toList());
        List<Move> allMoves = pieces.stream()
            .flatMap(p -> p.allPossibleMoves(getBoard()).stream())
            .collect(Collectors.toList());

        for (IChessPiece piece : pieces) {
            List<Move> sjadamJumps = getSjadamJumps(piece);
            Position oldPos = getLastJumpedPosition();
            if (!lastJumpedPieceWasEnemy() || oldPos == null) {
                allMoves.addAll(sjadamJumps);
            }
            else {
                allMoves.add(new Move(piece.getPosition(), oldPos, board));
            }
        }
        
        // THIS IS A HACK
        // NEVER DO THIS
        // NEVER TELL ANYONE I TOLD YOU THIS IS OKAY
        // I DIDN'T
        // I SHOULDN'T EVEN SHOW YOU THIS
        // I'M JUST GONNA SHOW MYSELF OUT
        final Map<Position, List<IChessPiece>> burned = new HashMap<>();
        return allMoves.stream()
            .filter(m -> {
                IChessPiece p = m.getPiece(board);
                List<IChessPiece> b = burned.computeIfAbsent(m.getPosition(), k -> new ArrayList<>());
                if (b.contains(p)) {
                    return false;
                }
                b.add(p);
                return true;
            }).map(m -> {
                IChessPiece p = m.getPiece(board);  // the moved piece
                Position np = m.getPosition();      // new position
                Position op = m.getOldPosition();   // old position

                Move ret = m;
                if (p.isOnEnemyRank(np) && !(p instanceof ChessPieceKing)) {
                    ret = new PromotionMove(op, np, board, new ChessPieceQueen(np, p.getColor()));
                }
                return ret;
            }).collect(Collectors.toList());
    }

    /**
     * Helper method: get all the possible jumps the piece can make.
     *
     * Does not do any validity checks.
     *
     * @param piece The piece to get jumps for
     * @return All jumps the piece can make
     */
    private List<Move> getSjadamJumps(IChessPiece piece) {
        List<Move> moves = new ArrayList<>();
        tryAddSjadamMove(piece, p -> p.south(1), moves);
        tryAddSjadamMove(piece, p -> p.north(1), moves);
        tryAddSjadamMove(piece, p -> p.west(1), moves);
        tryAddSjadamMove(piece, p -> p.east(1), moves);
        tryAddSjadamMove(piece, p -> p.northwest(1), moves);
        tryAddSjadamMove(piece, p -> p.northeast(1), moves);
        tryAddSjadamMove(piece, p -> p.southwest(1), moves);
        tryAddSjadamMove(piece, p -> p.southeast(1), moves);
        return moves;
    }

    /**
     * Helper method: add move jumping over piece if possible.
     */
    private void tryAddSjadamMove(IChessPiece piece, Position.Manipulator dir, List<Move> list) {
        Position p = piece.getPosition();
        Position next = dir.transform(p);
        Position next2 = dir.transform(next);
        if (isOutsideBoard(next2)) return;
        if (board.isOccupied(next) && ! board.isOccupied(next2))
            list.add(new Move(p, next2, board));
    }

    /**
     * Get a list of all possible moves a given piece can make this turn.
     *
     * Takes care of any restrictions put in place due to stateful changes in
     * the SjadamGame.
     *
     * @return A list of the possible moves that can be made by the current
     * player using this piece.
     */
    @Override
    public List<Move> allPossibleMoves(IChessPiece piece) {
        return allPossibleMoves().stream()
            .filter(m -> m.getPiece(board) == piece)
            .collect(Collectors.toList());
    }

    /**
     * Validate and perform a move.
     * @param from Coordinate from
     * @param to Coordinate to
     */
    public void performMove(Position from, Position to) {
        IChessPiece piece = getPieceAtPosition(from);
        if (piece == null) return;
        List<Move> movesToPos = allPossibleMoves(piece).stream()
            .filter(m -> m.getPosition().equals(to))
            .collect(Collectors.toList());
        
        if (movesToPos.size() == 1) {
            Move move = movesToPos.get(0);
            performMove(move);
        }
    }

    @Override
    public void performMove(Move move) {
        Color col = nextPlayerColor();
        IChessPiece piece = move.getPiece(board);
        boolean endTurn = piece.canMoveTo(move.getPosition(), board);
        if (endTurn) { // this is a regular chess move
            col = col.opponentColor(); // end turn
        }
        super.performMove(move);
        
        Position oldPos = getLastJumpedPosition();
        if (endTurn || !jumpedFromPositions.isEmpty()
                && move.getPosition().equals(jumpedFromPositions.get(0))) {
            jumpedFromPositions = new ArrayList<>(); // clear jumped positions
        }
        else if (move.getPosition().equals(oldPos)) {
            // drop last position from list of jumped positions
            jumpedFromPositions.remove(jumpedFromPositions.size()-1);
        }
        else {
            // put old piece position in list of jumped positions
            jumpedFromPositions.add(move.getOldPosition());
        }
        nextPlayerColor = col; // set next player color to the correct color
    }

    @Override
    public boolean canMoveTo(IChessPiece piece, Position pos) {
        return allPossibleMoves(piece).stream().anyMatch(m -> m.getPosition().equals(pos));
    }

    @Override
    public SjadamGame copy() {
        throw new RuntimeException("Copy not implemented for sjadam");
    }

    @Override
    public List<Move> getMoves(Position from, Position to) {
        return allPossibleMoves().stream()
            .filter(m -> m.getOldPosition().equals(from) && m.getPosition().equals(to))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isGameOver() {
        return super.isGameOver()
            || getPieces().stream().filter(ChessPieceKing.class::isInstance).count() < 2
            || allPossibleMoves().size() == 0;
    }

    public void endTurn() {
        nextPlayerColor = nextPlayerColor.opponentColor();
        jumpedFromPositions = new ArrayList<>(); // clear jumped positions
    }
    
    public boolean hasJumped() {
        return getLastJumpedPosition() != null;
    }
}

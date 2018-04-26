package com.syntax_highlighters.chess;

import java.util.List;
import com.syntax_highlighters.chess.entities.IChessPiece;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.syntax_highlighters.chess.entities.Color;

/**
 * Custom game mode with sjadam rules.
 *
 * https://github.com/JonasTriki/sjadam-js
 * www.sjadam.no
 */
public class SjadamGame extends AbstractGame {
    private IChessPiece jumpingPiece;

    public SjadamGame() {
        this.board = new Board();
        this.board.setupNewGame();
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
        // TODO: implement

        List<IChessPiece> pieces = getPieces();
        List<Move> allMoves = pieces.stream()
            .filter(p -> p.getColor() == nextPlayerColor()
                    && (jumpingPiece == null || jumpingPiece == p))
            .flatMap(p -> p.allPossibleMoves(getBoard()).stream())
            .collect(Collectors.toList());

        for (IChessPiece piece : pieces) {
            tryAddSjadamMove(piece, p -> p.south(1), allMoves);
            tryAddSjadamMove(piece, p -> p.north(1), allMoves);
            tryAddSjadamMove(piece, p -> p.west(1), allMoves);
            tryAddSjadamMove(piece, p -> p.east(1), allMoves);
            tryAddSjadamMove(piece, p -> p.northwest(1), allMoves);
            tryAddSjadamMove(piece, p -> p.northeast(1), allMoves);
            tryAddSjadamMove(piece, p -> p.southwest(1), allMoves);
            tryAddSjadamMove(piece, p -> p.southeast(1), allMoves);
        }
        return allMoves;
    }

    /**
     * Helper method: add move jumping over piece if possible.
     */
    private void tryAddSjadamMove(IChessPiece piece, Position.Manipulator dir, List<Move> list) {
        Position p = piece.getPosition();
        Position next = dir.transform(p);
        Position next2 = dir.transform(next);
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

    @Override
    public List<Move> performMove(Position from, Position to) {
        IChessPiece piece = getPieceAtPosition(from);
        if (piece == null) return new ArrayList<>();
        List<Move> movesToPos = allPossibleMoves(piece).stream()
            .filter(m -> m.getPosition().equals(to))
            .collect(Collectors.toList());
        
        if (movesToPos.size() == 1) {
            Move move = movesToPos.get(0);
            Color col = nextPlayerColor();
            if (piece.canMoveTo(to, board)) {
                col = col.opponentColor(); // end turn
                jumpingPiece = null; // piece not jumping anymore
            }
            else if (jumpingPiece == null) {
                System.out.println("Setting jumping piece to " + piece);
                jumpingPiece = piece; // this piece starts jumping
            }
            else if (jumpingPiece != piece) {
                throw new IllegalStateException("Jumping piece isn't this piece");
            }
            performMove(move);
            nextPlayerColor = col;
        }
        return movesToPos;
    }

    @Override
    public boolean canMoveTo(IChessPiece piece, Position pos) {
        return allPossibleMoves(piece).stream().anyMatch(m -> m.getPosition().equals(pos));
    }
}

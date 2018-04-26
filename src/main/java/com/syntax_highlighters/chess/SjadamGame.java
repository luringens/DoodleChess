package com.syntax_highlighters.chess;

import java.util.List;
import com.syntax_highlighters.chess.entities.IChessPiece;
import java.util.ArrayList;

/**
 * Custom game mode with sjadam rules.
 *
 * https://github.com/JonasTriki/sjadam-js
 * www.sjadam.no
 */
public class SjadamGame extends AbstractGame {

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

        List<Move> allMoves = new ArrayList<>();
        List<IChessPiece> pieces = getPieces();

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
        return null;
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
        //TODO: implement
        return null;
    }
}

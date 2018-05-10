package com.syntax_highlighters.chess.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.EnPassantMove;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.PromotionMove;

/**
 * Pawn chess piece.
 */
public class ChessPiecePawn extends AbstractChessPiece {
    /**
     * Create a pawn at the given position with the given color.
     *
     * @param pos The position to place the pawn at
     * @param color The color of the piece
     */
    public ChessPiecePawn (Position pos, Color color) {
        super(pos, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toChessNotation() {
        return ""; // pawns are denoted by their lack of a letter
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
             0,  0,   0,   0,   0,   0,  0,  0,
            50, 50,  50,  50,  50,  50, 50, 50,
            10, 10,  20,  30,  30,  20, 10, 10,
             5,  5,  10,  25,  25,  10,  5,  5,
             0,  0,   0,  20,  20,   0,  0,  0,
             5, -5, -10,   0,   0, -10, -5,  5,
             5, 10,  10, -20, -20,  10, 10,  5,
             0,  0,   0,   0,   0,   0,  0,  0
        };
    }

    /**
     * Return all possible moves the pawn can make.
     *
     * A pawn may move two steps forward if it hasn't moved before, otherwise
     * one step forward unless its path is blocked by another piece. It may also
     * capture an enemy piece diagonally forward.
     *
     * On the occasion that the pawn is on the fifth rank (if it's white - the
     * fourth rank if it's black) and an enemy pawn uses a double step to the
     * square beside it, the pawn may move as if the pawn had only taken a
     * single step and capture the piece. This is only possible the first turn
     * after the enemy pawn has moved.
     *
     * On the occasion that a pawn reaches the far side of the board (on the
     * side of the enemy), the pawn shall be replaced with a queen.
     *
     * @param board The current state of the board
     *
     * @return A List of all the possible moves the piece can make
     */
    @Override
    public List<Move> allPossibleMoves (Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Position pos = getPosition();
        
        // one step forward
        Position f1 = this.forward(1);
        
        // check if one step forward can be performed
        if (board.isOnBoard(f1) && !board.isOccupied(f1)) {
            addRegularMove(board, possibleMoves, f1);
            
            // two steps forward
            Position f2 = this.forward(2);
            
            // if one step forward can be performed, check if two steps forward
            // can be performed
            if (!this.hasMoved() && board.isOnBoard(f2) && !board.isOccupied(f2)) {
                addRegularMove(board, possibleMoves, f2);
            }
        }

        // add move to take enemies, if possible
        takeEnemiesMove(board, possibleMoves, this.forward(1).east(1));
        takeEnemiesMove(board, possibleMoves, this.forward(1).west(1));
        
        // add move to perform en passant, if possible
        enPassantCheck(board, possibleMoves, pos.east(1));
        enPassantCheck(board, possibleMoves, pos.west(1));

        return possibleMoves.stream()
                .filter(m -> board.moveDoesntPutKingInCheck(m, color))
                .collect(Collectors.toList());

    }

    /**
     * If en passant can be performed onto the given position, add the move to
     * possible moves.
     *
     * @param board The current state of the board
     * @param possibleMoves Reference to the list of possible moves
     * @param pos The position to perform en passant onto
     */
    private void enPassantCheck (Board board, ArrayList<Move> possibleMoves, Position pos) {
        if (board.isOnBoard(pos)) {
            // en passant can only be performed at a pawn's fifth rank
            if (color.isWhite() && pos.getY() != 5) return;
            if (color.isBlack() && pos.getY() != 4) return; // this is black's "fifth rank"
            
            IChessPiece pieceAtPos = board.getAtPosition(pos);
            Move lastMove = board.getLastMove();
            
            if (lastMove == null) return; // pawn can't possibly have just moved there
            if (pieceAtPos == null) return; // there is no piece at that position, enemy or friend
            if (lastMove.getPiece(board) != pieceAtPos) return; // the piece wasn't the last moved piece
            if (!(pieceAtPos instanceof ChessPiecePawn)) return; // piece isn't pawn
            
            // check that the last move was a double step
            Position newPos = lastMove.getPosition();
            Position oldPos = lastMove.getOldPosition();
            if (Math.abs(newPos.getY() - oldPos.getY()) != 2) return; // last move was a single step
            
            // en passant can be performed
            if (color.isWhite()) {
                possibleMoves.add(new EnPassantMove(this.getPosition(), pos.north(1), board, pieceAtPos));
            }
            else {
                possibleMoves.add(new EnPassantMove(this.getPosition(), pos.south(1), board, pieceAtPos));
            }
        }
    }

    /**
     * If it's possible to capture an enemy normally by moving onto the given
     * position, add that move to possible moves.
     *
     * @param board The current state of the board
     * @param possibleMoves Reference to the list of possible moves
     * @param pos The position to perform capture onto
     */
    private void takeEnemiesMove (Board board, ArrayList<Move> possibleMoves, Position pos) {
        if (board.isOnBoard(pos)) {
            if (board.isEnemy(this, pos)) {
                addRegularMove(board, possibleMoves, pos);
            }
        }
    }

    /**
     * Helper method: if position is on end of board, add four PromotionMoves,
     * else add one regular move.
     *
     * @param board The board
     * @param possibleMoves The list to add the move(s) to
     * @param pos The position the move(s) lead to
     */
    private void addRegularMove(Board board, ArrayList<Move> possibleMoves, Position to) {
        Position from = getPosition();
        if (to.getY() == 1 || to.getY() == 8) {
            Color c = this.getColor();
            possibleMoves.add(new PromotionMove(from, to, board, new ChessPieceQueen(to, c)));
            possibleMoves.add(new PromotionMove(from, to, board, new ChessPieceRook(to, c)));
            possibleMoves.add(new PromotionMove(from, to, board, new ChessPieceBishop(to, c)));
            possibleMoves.add(new PromotionMove(from, to, board, new ChessPieceKnight(to, c)));
        }
        else {
            possibleMoves.add(new Move(from, to, board));
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IChessPiece copy () {
        return new ChessPiecePawn(this.getPosition(), this.getColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPieceScore() {
        return 100;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAssetName () {
        return "pawn_white.png";
    }

    /**
     * Helper method: return the position n steps forward.
     *
     * No bounds checks. Works correctly depending on whether your are black or
     * white.
     *
     * @return The position n steps forward
     */
    private Position forward(int nSteps) {
        if (color.isWhite()) return this.getPosition().north(nSteps);
        return this.getPosition().south(nSteps);
    }

    /**
     * Determine whether this piece threatens a given position.
     *
     * This method is not entirely accurate. Specifically, it does not
     * consider the move en passant, because to correctly handle that move, it
     * will need to know the intent of the caller - that is to say, whether the
     * caller is a pawn which is attempting to move two steps forward the next
     * turn or not.
     *
     * Since the alternative is to indicate that a lot of positions
     * are threatened which really aren't, and since en passant being possible
     * is quite a rare occurrence, and since the most important function of this
     * method is to determine whether the king will be safe or not, for which
     * the en passant rule does not apply anyways, this controlled inaccuracy of
     * the method is allowed.
     *
     * @param p The position to consider
     * @param b The current board state
     *
     * @return true if the piece threatens the position, false otherwise
     */
    @Override
    public boolean threatens(Position p, Board b) {
        return p.equals(forward(1).east(1)) || p.equals(forward(1).west(1));
            // en passant
            // NOTE: This part is actually broken, for several reasons:
            // 1. only applies to pawns having just moved two steps forward, but
            //    there is no way to indicate this to the caller
            // 2. this pawn must be on 5th rank if they're white, or 4th rank if
            //    they're black
            //
            // For these reasons, the en passant check has been commented out,
            // as it's *probably* not a huge problem. I've kept the code here for
            // purpose of documentation of the approaches tried.
            //|| p.equals(position.east(1)) && b.getAtPosition(forward(1).east(1)) == null
            //|| p.equals(position.west(1)) && b.getAtPosition(forward(1).west(1)) == null;
    }
}

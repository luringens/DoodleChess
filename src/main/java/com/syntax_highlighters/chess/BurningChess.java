package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BurningChess extends AbstractGame{
    private float blackTimer = 0;
    private float whiteTimer = 0;
    private static final float TENMINS = 5 * 1;

    public BurningChess(AiDifficulty whiteAi, AiDifficulty blackAi) {
        if (whiteAi != null) {
            MiniMaxAIPlayer ai = new MiniMaxAIPlayer(whiteAi);
            this.whiteAI = new AsyncPlayer(ai);
        }
        if (blackAi != null) {
            MiniMaxAIPlayer ai = new MiniMaxAIPlayer(blackAi);
            this.blackAI = new AsyncPlayer(ai);
        }

        this.board = new Board();
        this.board.setupNewGame();
    }

    public BurningChess(Board board, Color nextPlayerColor) {
        this.board = board;
        this.nextPlayerColor = nextPlayerColor;
    }

    public static BurningChess setupTestBoard(Board board, Color nextPlayerColor) {
        return new BurningChess(board, nextPlayerColor);
    }

    List<Position> unreachablePos = new ArrayList<>();

    public void fireTimer(float seconds){
        if(nextPlayerColor.isWhite()) {
            whiteTimer += seconds;
        }
        if(nextPlayerColor.isBlack()) {
            blackTimer += seconds;
        }
    }

    public float getWhiteTimer() { return whiteTimer / TENMINS; }
    public float getBlackTimer() { return blackTimer / TENMINS; }

    public List<Position> tileUnreachable(){
        return unreachablePos;
    }

    public IChessPiece killTile(Position tile) {
        IChessPiece piece = getPieceAtPosition(tile);
        if (piece != null)
            killPiece(piece);
        unreachablePos.add(tile);
        return piece;
    }


    public void killPiece(IChessPiece piece) {
        if(piece instanceof ChessPieceKing)
            forceGameEnd();
        board.removePiece(piece);
        pieceSplash(piece);
    }

    public void reviveTile(Position tile) {
        if (unreachablePos.contains(tile))
            unreachablePos.remove(tile);
    }

    public void pieceSplash(IChessPiece piece){
        unreachablePos = new ArrayList<>();
    }

    @Override
    public List<Move> allPossibleMoves() {
        return getPieces().stream()
                .filter(p -> p.getColor() == nextPlayerColor())
                .flatMap(p -> p.allPossibleMoves(getBoard()).stream())
                .filter(m -> !unreachablePos.contains(m.getPosition()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Move> allPossibleMoves(IChessPiece piece) {
        if (piece.getColor() != nextPlayerColor())
            return new ArrayList<>(); // piece cannot move
        return piece.allPossibleMoves(getBoard()).stream()
                .filter(m -> !unreachablePos.contains(m.getPosition()))
                .collect(Collectors.toList());
    }


    /**
     * Checks if game is over.
     * <p>
     * Deliberately ignores fifty-move rule and threefold repetition (at least
     * for the time being).
     *
     * @return true if the game is over, false otherwise
     */
    @Override
    public boolean isGameOver() {
        return super.isGameOver()
                || board.checkMate(Color.WHITE)
                || board.checkMate(Color.BLACK)
                || insufficientMaterial()
                || board.getAllPieces().stream()
                .filter(p -> p.getColor() == nextPlayerColor)
                .noneMatch(p -> p.allPossibleMoves(board).size() > 0);
    }

    /**
     * Check whether the board contains insufficient material (automatic draw).
     * <p>
     * Insufficient material is true if any of these conditions hold:
     * - king vs king
     * - king vs king and bishop
     * - king vs king and knight
     * - king and bishop vs king and bishop, bishops are on same colored square
     * <p>
     * For the purpose of this method, we'll also say that a game with less than
     * two kings contains insufficient material, for obvious reasons.
     * <p>
     * NOTE: Could be more efficient with early return, but I did it like this
     * to increase readability.
     *
     * @return true if the game should be drawn due to insufficient material,
     * false otherwise
     */
    public boolean insufficientMaterial() {
        List<IChessPiece> pieces = board.getAllPieces();

        List<IChessPiece> kings = pieces.stream()
                .filter(p -> p instanceof ChessPieceKing)
                .collect(Collectors.toList());

        List<IChessPiece> knights = pieces.stream()
                .filter(p -> p instanceof ChessPieceKnight)
                .collect(Collectors.toList());

        List<IChessPiece> bishops = pieces.stream()
                .filter(p -> p instanceof ChessPieceBishop)
                .collect(Collectors.toList());

        // obviously
        return kings.size() < 2
                || pieces.size() == kings.size()
                || pieces.size() == 3 && bishops.size() == 1
                || pieces.size() == 3 && knights.size() == 1
                || pieces.size() == 4 && bishops.size() == 2
                && sameColoredSquare(bishops.get(0).getPosition(), bishops.get(1).getPosition());

    }
    @Override
    public AbstractGame copy() {
        BurningChess copy = new BurningChess(board.copy(), nextPlayerColor);
        copy.whiteAI = whiteAI;
        copy.blackAI = blackAI;
        copy.gameOver = gameOver;
        copy.moveHistory = copyMoveHistory();
        copy.whiteTimer = whiteTimer;
        copy.blackTimer = blackTimer;
        copy.unreachablePos = new ArrayList<>(unreachablePos);
        return copy;
    }

    @Override
    public boolean canMoveTo(IChessPiece piece, Position pos) {
        return piece.canMoveTo(pos, board) && !unreachablePos.contains(pos);
    }
}


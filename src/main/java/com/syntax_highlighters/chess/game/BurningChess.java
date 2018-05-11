package com.syntax_highlighters.chess.game;

import com.syntax_highlighters.chess.AsyncPlayer;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Color;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.ai.AiDifficulty;
import com.syntax_highlighters.chess.ai.MiniMaxAIPlayer;
import com.syntax_highlighters.chess.chesspiece.*;
import com.syntax_highlighters.chess.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fire Chess game mode variant: As the board burns, more and more of the tiles
 * are rendered unusable, and pieces consumed by the flames disappear in a
 * blaze, blasting the flames clear of their immediate surroundings.
 */
public class BurningChess extends AbstractGame{
    private float blackTimer = 0;
    private float whiteTimer = 0;
    private static final float TENMINS = 60 * 10.0f;
    public static final float SPLASHTIME = 30.0f; // in seconds
    private List<Position> unreachablePos = new ArrayList<>();

    /**
     * Create a new BurningChess instance.
     *
     * NOTE: The AI players are unused, should be removed.
     *
     * @param whiteAi The difficulty of the white AI player (unused)
     * @param blackAi The difficulty of the black AI player (unused)
     */
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

    /**
     * Helper constructor: Create a game with the given board and set which
     * player has the first move.
     *
     * @param board The state of the board the game should start in
     * @param nextPlayerColor The color who should have the first move
     */
    private BurningChess(Board board, Color nextPlayerColor) {
        this.board = board;
        this.nextPlayerColor = nextPlayerColor;
    }

    /**
     * Set up a BurningChess board for testing purposes.
     *
     * Do not use except in tests.
     *
     * @return A BurningChess game with the given board and starting color
     */
    public static BurningChess setupTestBoard(Board board, Color nextPlayerColor) {
        return new BurningChess(board, nextPlayerColor);
    }

    /**
     * Increment the timer of the currently active player with the given delta
     * value.
     *
     * @param seconds The delta value in seconds
     */
    public void fireTimer(float seconds){
        if(nextPlayerColor.isWhite()) {
            whiteTimer += seconds;
        }
        if(nextPlayerColor.isBlack()) {
            blackTimer += seconds;
        }
    }

    /**
     * Get the timer value of the white player.
     *
     * @return The timer value of the white player as a percentage of ten
     * minutes
     */
    public float getWhiteTimer() { return whiteTimer / TENMINS; }
    
    /**
     * Get the timer value of the black player.
     *
     * @return The timer value of the black player as a percentage of ten
     * minutes
     */
    public float getBlackTimer() { return blackTimer / TENMINS; }

    /**
     * Retrieve the list of unreachable positions.
     *
     * @return The list of positions which have been consumed by the roaring
     * inferno
     */
    public List<Position> tileUnreachable(){
        return unreachablePos;
    }

    /**
     * Let the given tile be destroyed in fire.
     *
     * If a piece was standing on the tile, remove the piece and create a
     * splash.
     *
     * @param tile The tile to destroy
     * @return The piece that was standing on the tile, if any
     */
    public IChessPiece killTile(Position tile) {
        IChessPiece piece = getPieceAtPosition(tile);
        if (piece != null)
            killPiece(piece);
        unreachablePos.add(tile);
        return piece;
    }

    /**
     * Remove the given piece from the board and create a splash.
     *
     * If the removed piece is the king, the game is over.
     *
     * @param piece The piece to remove
     */
    public void killPiece(IChessPiece piece) {
        if(piece instanceof ChessPieceKing)
            forceGameEnd();
        board.removePiece(piece);
        pieceSplash();
    }

    /**
     * Allow a specific tile to be revived (and thus made reachable again).
     * 
     * NOTE: unused
     *
     * @param tile The tile to make reachable
     */
    @Deprecated
    public void reviveTile(Position tile) {
        unreachablePos.remove(tile);
    }

    /**
     * Reset all unreachable positions.
     *
     * NOTE: This works because the next time the FireOverlay interacts with the
     * game, it will determine which parts of the board are still covered in
     * flames and kill them again right away. Thus, this only breaks the game's
     * state *briefly*.
     */
    private void pieceSplash(){
        unreachablePos = new ArrayList<>();
    }

    /**
     * Get a list of all the possible moves that can be made during this turn.
     *
     * Filters the regular chess moves by current player as well as not leading
     * to an unreachable position.
     *
     * @return A list of all the possible moves that can be made by the current
     * player.
     */
    @Override
    public List<Move> allPossibleMoves() {
        return getPieces().stream()
                .filter(p -> p.getColor() == nextPlayerColor())
                .flatMap(p -> p.allPossibleMoves(getBoard()).stream())
                .filter(m -> !unreachablePos.contains(m.getPosition()))
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all possible moves a given piece can make this turn.
     *
     * If the piece does not belong to the current player, no moves are possible
     * with that piece. Otherwise, filter the piece's moved by not leading to an
     * unreachable position.
     *
     * @param piece The piece to get the moves of
     * @return A list of the possible moves that can be made by the current
     * player using this piece.
     */
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
    private boolean insufficientMaterial() {
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canMoveTo(IChessPiece piece, Position pos) {
        return piece.canMoveTo(pos, board) && !unreachablePos.contains(pos);
    }
}


package com.syntax_highlighters.chess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.syntax_highlighters.chess.AsyncPlayer;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.ai.AiDifficulty;
import com.syntax_highlighters.chess.chesspiece.ChessPieceBishop;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKing;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKnight;
import com.syntax_highlighters.chess.Color;
import com.syntax_highlighters.chess.ai.IAiPlayer;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;
import com.syntax_highlighters.chess.ai.MiniMaxAIPlayer;
import com.syntax_highlighters.chess.move.Move;

/**
 * Game class keeping track of the game state.
 *
 * Contains a Board, as well as the two players, white and black. Keeps track of
 * which player's turn it is. The UI uses the API of this class to interact with
 * the game.
 */
public class ChessGame extends AbstractGame {

    /**
     * Creates a new, blank game and sets up the board.
     *
     * @param whiteAi The difficulty for the white AI, or `null` for no AI
     * @param blackAi The difficulty for the black AI, or `null` for no AI
     */
    public ChessGame(AiDifficulty whiteAi, AiDifficulty blackAi) {
        if (whiteAi != null) {
            IAiPlayer ai = new MiniMaxAIPlayer(whiteAi);
            this.whiteAI = new AsyncPlayer(ai);
        }
        if (blackAi != null) {
            IAiPlayer ai = new MiniMaxAIPlayer(blackAi);
            this.blackAI = new AsyncPlayer(ai);
        }

        this.board = new Board();
        this.board.setupNewGame();
    }

    /**
     * Helper constructor: Create a game with the given board state and set the
     * color of the starting player.
     */
    private ChessGame(Board board, Color nextPlayerColor) {
        this.board = board;
        this.nextPlayerColor = nextPlayerColor;
    }

    /**
     * Set up a game with a board for testing purposes.
     * **DO NOT USE FOR ACTUAL GAMES**.
     * @param board The board to use
     * @param nextPlayerColor The next player that will make a move.
     * @return An instance of the board in a blank state with nextplayercolor set.
     */
    public static ChessGame setupTestBoard(Board board, Color nextPlayerColor) {
        return new ChessGame(board, nextPlayerColor);
    }

    /**
     * Checks if game is over.
     *
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
                || board.getAllPieces(nextPlayerColor).stream()
                .noneMatch(p -> p.allPossibleMoves(board).size() > 0);
    }

    /**
     * Check whether the board contains insufficient material (automatic draw).
     *
     * Insufficient material is true if any of these conditions hold:
     *  - king vs king
     *  - king vs king and bishop
     *  - king vs king and knight
     *  - king and bishop vs king and bishop, bishops are on same colored square
     *
     * For the purpose of this method, we'll also say that a game with less than
     * two kings contains insufficient material, for obvious reasons.
     *
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
    public List<Move> allPossibleMoves() {
        return board.getAllPieces(nextPlayerColor).stream()
            .flatMap(p -> p.allPossibleMoves(getBoard()).stream())
            .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Move> allPossibleMoves(IChessPiece piece) {
        if (piece.getColor() != nextPlayerColor()) return new ArrayList<>(); // piece cannot move
        return piece.allPossibleMoves(getBoard());
    }

    /**
     * {inheritDoc}
     */
    @Override 
    public boolean canMoveTo(IChessPiece piece, Position pos) {
        return piece.canMoveTo(pos, board);
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
	public AbstractGame copy() {
        ChessGame copy = new ChessGame(board.copy(), nextPlayerColor);
        copy.whiteAI = whiteAI;
        copy.blackAI = blackAI;
        copy.gameOver = gameOver;
        copy.moveHistory = copyMoveHistory();
		return copy;
	}
}

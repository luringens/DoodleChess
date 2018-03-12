package com.syntax_highlighters.chess;

import java.util.List;
import java.util.ArrayList;

import com.syntax_highlighters.chess.entities.IPlayer;

/**
 * Game class keeping track of the game state.
 *
 * Contains a Board, as well as the two players, white and black. Keeps track of
 * which player's turn it is. Records move history. The UI uses the API of this
 * class to interact with the game.
 */
public class Game {
    private Board board;
    private IPlayer white;
    private IPlayer black;
    private List<Move> history;

    /**
     * Constructor.
     *
     * Creates a new, blank game and sets up the board.
     *
     * @param white The white player
     * @param black The black player
     */
    public Game(IPlayer white, IPlayer black) {
        this.white = white;
        this.black = black;
        
        this.board = new Board();
        this.history = new ArrayList<>();
        
        this.board.setupNewGame(white, black);
    }
}

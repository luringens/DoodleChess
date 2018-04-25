package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.Color;

/**
 * Custom game mode with sjadam rules.
 *
 * https://github.com/JonasTriki/sjadam-js
 * www.sjadam.no
 */
public class SjadamGame extends AbstractGame {
    /**
     * {@inheritDoc}
     */
    public int getWinner() {
        if (board.checkMate(Color.WHITE)) return -1;
        if (board.checkMate(Color.BLACK)) return 1;
        return 0;
    }
}

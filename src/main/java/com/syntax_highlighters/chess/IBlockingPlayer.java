package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.game.AbstractGame;
import com.syntax_highlighters.chess.move.Move;

public interface IBlockingPlayer {
    Move GetMove(AbstractGame game);
}
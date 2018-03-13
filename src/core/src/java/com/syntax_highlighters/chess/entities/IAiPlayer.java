package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;

public interface IAiPlayer {
    void PerformMove(Board board);
}

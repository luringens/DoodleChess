package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;

public interface IAiPlayer {
    void SetDifficulty(AiDifficulty diff);
    void PerformMove(Board board);
}


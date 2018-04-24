package com.syntax_highlighters.chess.entities;

/**
 * Enum for AI difficulty.
 *
 * Supports three different degrees of difficulty: Easy, Medium and Hard.
 */
public enum AiDifficulty {
    // Doesn't see far, makes a lot of mistakes
    Easy,
    // Makes some mistakes
    Medium,
    // Does not make mistakes
    Hard,
    // Doesn't see far, but does not make mistakes either
    ShortSighted
}

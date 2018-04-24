package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.badlogic.gdx.graphics.Color;

/**
 * Contains information about the player which the GUI uses to determine things
 * such as the color of the pieces, the account name, or the AI  difficulty, if
 * applicable.
 */
public class PlayerAttributes {
    private Account acc;
    private AiDifficulty difficulty;
    private Color color;

    /**
     * Define a human player with an associated Account and Color.
     *
     * @param acc The account of the player
     * @param color The color of the player's pieces
     */
    public PlayerAttributes(Account acc, Color color) {
        this.acc = acc;
        this.color = color;
    }

    /**
     * Define a machine player with an associated AiDifficulty and Color.
     *
     * @param difficulty The difficulty of the AI player
     * @param color The color of the player's pieces
     */
    public PlayerAttributes(AiDifficulty difficulty, Color color) {
        this.difficulty = difficulty;
        this.color = color;
    }

    /**
     * Retrieve the color of the player's pieces.
     *
     * @return The color set by the constructor
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Retrieve the account information of the player, if applicable.
     *
     * @return The account set in the constructor, or null if the AiDifficulty
     * constructor was used
     */
    public Account getAccount() {
        return this.acc;
    }

    /**
     * Retrieve the AI difficulty information of the player, if applicable.
     *
     * @return The AI difficulty set in the constructor, or null if the Account
     * constructor was used
     */
    public AiDifficulty getAIDifficulty() {
        return this.difficulty;
    }
}

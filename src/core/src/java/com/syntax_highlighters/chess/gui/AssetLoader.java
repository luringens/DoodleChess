package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
    public static void LoadAssets(AssetManager manager)
    {
        String[] textures = {
                "pawn_white.png",
                "bishop_white.png",
                "king_white.png",
                "knight_white.png",
                "queen_white.png",
                "rook_white.png"
        };

        for(String path : textures)
        {
            manager.load(path, Texture.class);
        }
    }
}

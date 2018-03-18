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
                "rook_white.png",
                "pawn_black.png",
                "bishop_black.png",
                "king_black.png",
                "knight_black.png",
                "queen_black.png",
                "rook_black.png",
                "paper.png",
                "tile.png",
                "tile_black.png"
        };

        for(String path : textures)
        {
            manager.load(path, Texture.class);
        }
    }
}

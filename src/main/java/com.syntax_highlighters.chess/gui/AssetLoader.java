package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Helper class which loads the assets used in the game.
 */
public class AssetLoader {
    public static BitmapFont font;

    public static BitmapFont GetDefaultFont(AssetManager assetManager)
    {
        return font;
    }

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
                "tile_black.png",
                "tile_highlight.png",
                "button_template.png",
                "pixel.png",
                "overlay.png",
                "background2.png",
                "pencils/base1.png",
                "pencils/base2.png",
                "pencils/wood1.png",
                "pencils/wood2.png",
                "pencils/wood3.png",
                "mutebutton.png",
        };

        for(String path : textures)
        {
            manager.load(path, Texture.class);
        }

        Texture texture = new Texture(Gdx.files.internal("IndieFlower.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("IndieFlower.fnt"), new TextureRegion(texture), false);
        manager.load("tick.wav",Sound.class);
        manager.load("kho.wav",Sound.class);
        manager.load("chesstheme.wav",Music.class);

    }

    public static String getAccountPath() {
        return "accounts.data";
    }
}

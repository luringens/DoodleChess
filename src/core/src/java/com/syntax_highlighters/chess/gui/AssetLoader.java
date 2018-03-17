package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
    public static void LoadAssets(AssetManager manager)
    {
        String[] textures = {
                "bondefinal.png",
                "bishopfinal.png",
                "kingfinal.png",
                "hestfinal.png",
                "queenfinal.png",
                "tårnetfinal.png"
        };

        for(String path : textures)
        {
            manager.load(path, Texture.class);
        }
    }
}

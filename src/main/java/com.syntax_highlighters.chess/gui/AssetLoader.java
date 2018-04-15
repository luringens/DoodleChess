package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Helper class which loads the assets used in the game.
 */
public class AssetLoader {
    public static BitmapFont font;

    public static BitmapFont GetDefaultFont(AssetManager assetManager)
    {
        return assetManager.get("font24.ttf");
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
                "soundbutton.png",
        };

        for(String path : textures)
        {
            manager.load(path, Texture.class);
        }

        manager.load("tick.wav",Sound.class);
        manager.load("kho.wav",Sound.class);
        manager.load("chesstheme.wav",Music.class);
        
        // set the loaders for the generator and the fonts themselves
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));


        FreetypeFontLoader.FreeTypeFontLoaderParameter sizeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        sizeParams.fontFileName = "Gaegu-Bold.ttf";
        sizeParams.fontParameters.size = 24;

        manager.load("font24.ttf", BitmapFont.class, sizeParams);


        ShaderProgramLoader.ShaderProgramParameter shaderProgramParameter = new ShaderProgramLoader.ShaderProgramParameter();
        shaderProgramParameter.vertexFile = "shaders/id.vert";
        shaderProgramParameter.fragmentFile = "shaders/setColor.frag";
        manager.load("setColor.frag", ShaderProgram.class, shaderProgramParameter);

    }

    public static String getAccountPath() {
        return "accounts.data";
    }
}

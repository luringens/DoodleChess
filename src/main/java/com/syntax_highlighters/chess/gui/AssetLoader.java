package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Helper class which loads the assets used in the game.
 */
public class AssetLoader {
    public static BitmapFont font;

    public static BitmapFont GetDefaultFont(AssetManager assetManager) {
        return assetManager.get("font24.ttf");
    }

    public static BitmapFont GetDefaultFont(AssetManager assetManager, boolean big) {
        return big ? assetManager.get("fontBig.ttf") : assetManager.get("font24.ttf");
    }

    public static BitmapFont GetDefaultFont(AssetManager assetManager, int size) {
        if(!assetManager.isLoaded("font" + size + ".ttf"))
        {
            FreetypeFontLoader.FreeTypeFontLoaderParameter sizeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            sizeParams.fontFileName = "Gaegu-Bold.ttf";
            sizeParams.fontParameters.size = size;
            assetManager.load("font" + size + ".ttf", BitmapFont.class, sizeParams);
            assetManager.finishLoading();
        }
        return assetManager.get("font" + size + ".ttf");
    }

    public static void LoadAssets(AssetManager manager) {
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
                "selectBox.png",
                "pixel.png",
                "overlay.png",
                "background2.png",
                "pencils/base1.png",
                "pencils/base2.png",
                "pencils/wood1.png",
                "pencils/wood2.png",
                "pencils/wood3.png",
                "Doodle/eye.png",
                "Doodle/chessstars.png",
                "Doodle/food.png",
                "Doodle/Coffe.png",
                "Doodle/dragonball.png",
                "Doodle/Lhand.png",
                "Doodle/monkey.png",
                "Doodle/poop.png",
                "Doodle/smiley.png",
                "Doodle/TV.png",
                "Doodle/halloween.png",
                "Doodle/earth.png",
                "Doodle/dango.png",
                "Doodle/basketball.png",
                "Doodle/crowd.png",
                "Doodle/car.png",
                "Doodle/milk.png",
                "Doodle/fish.png",
                "Doodle/wheel.png",
                "Doodle/UFO.png",
                "Doodle/pawn.png",
                "Doodle/Cartman.png",
                "Doodle/water.png",
                "Doodle/batman.png",
                "Doodle/pear.png",
                "Doodle/Horse.png",
                "mutebutton.png",
                "soundbutton.png",
                "cursor.png",
                "listBackground.png",
                "transparent.png",
                "leaderboardsBackground.png",
                "circle.png",
                "dot.png",
                "square.png",
                "tick.png"
        };

        for (String path : textures) {
            manager.load(path, Texture.class);
        }

        manager.load("tick.wav", Sound.class);
        manager.load("kho.wav", Sound.class);
        manager.load("chesstheme.wav", Music.class);

        // set the loaders for the generator and the fonts themselves
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));


        FreetypeFontLoader.FreeTypeFontLoaderParameter sizeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        sizeParams.fontFileName = "Gaegu-Bold.ttf";
        sizeParams.fontParameters.size = 24;
        manager.load("font24.ttf", BitmapFont.class, sizeParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter sizeParams2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        sizeParams2.fontFileName = "Gaegu-Bold.ttf";
        sizeParams2.fontParameters.size = 42;
        manager.load("fontBig.ttf", BitmapFont.class, sizeParams2);


        ShaderProgramLoader.ShaderProgramParameter shaderProgramParameter = new ShaderProgramLoader.ShaderProgramParameter();
        shaderProgramParameter.vertexFile = "shaders/id.vert";
        shaderProgramParameter.fragmentFile = "shaders/setColor.frag";
        manager.load("setColor.frag", ShaderProgram.class, shaderProgramParameter);

        ShaderProgramLoader.ShaderProgramParameter wobbleParameters = new ShaderProgramLoader.ShaderProgramParameter();
        wobbleParameters.vertexFile = "shaders/id.vert";
        wobbleParameters.fragmentFile = "shaders/offset.frag";
        manager.load("wobble.frag", ShaderProgram.class, wobbleParameters);

        loadFragmentShader(manager, "fireNoise.frag");
        loadFragmentShader(manager, "fire.frag");
    }

    private static void loadFragmentShader(AssetManager manager, String name) {
        ShaderProgramLoader.ShaderProgramParameter wobbleParameters = new ShaderProgramLoader.ShaderProgramParameter();
        wobbleParameters.vertexFile = "shaders/id.vert";
        wobbleParameters.fragmentFile = "shaders/" + name;
        manager.load(name, ShaderProgram.class, wobbleParameters);
    }

    public static String getAccountPath() {
        return "UserAccounts.db";
    }
}

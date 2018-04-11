package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Audio {

    public static void menuSound(AssetManager manager){
        Sound sound = manager.get("tick.wav");
        sound.play();
    }

    public static void themeMusic(AssetManager manager, Boolean num){
        Music music = manager.get("chesstheme.wav");
        music.setVolume(0.5f);
        music.play();
        music.setLooping(true);
        if(num == false) music.setVolume(0.0f);

    }

    public static void makeMove(AssetManager manager){
        Sound sound = manager.get("kho.wav");
        sound.play();

    }

}

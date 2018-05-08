package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Audio {

    public static void menuSound(AssetManager manager){
        Sound sound = manager.get("tick.wav");
        sound.play(0.1f);
    }

    public static void themeMusic(AssetManager manager, Boolean num){
        Music music = manager.get("chesstheme.wav");
        music.setVolume(1.0f);
        music.play();
        music.setLooping(true);
        if(num == false) music.setVolume(0.0f);

    }

    public static void makeMove(AssetManager manager){
        Sound sound = manager.get("kho.wav");
        sound.play();

    }

    public static void SplashingSound(AssetManager manager){
        Sound sound = manager.get("WaterSplash.wav");
        sound.play(0.5f);
    }

}

package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.actors.PaperBackground;

public class LoadingScreen implements Screen {
    Texture logo;
    SpriteBatch batch = new SpriteBatch();
    LibgdxChessGame game;
    PaperBackground paper;

    float startWidth, startHeight;

    public LoadingScreen(LibgdxChessGame game) {
        logo = new Texture(Gdx.files.internal("loading.png"));
        this.game = game;
        game.getAssetManager().finishLoadingAsset("pixel.png");
        paper = new PaperBackground(game.getAssetManager());
        startWidth = Gdx.graphics.getWidth();
        startHeight = Gdx.graphics.getHeight();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(game.getAssetManager().update()) {
            game.finishedLoading();
            game.setScreen(new MainMenuScreen(game));
            return;
        }
        paper.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        paper.draw(batch, 1.0f);
        batch.draw(logo, startWidth / 2.f - logo.getWidth() / 2.f, startHeight / 2.f - logo.getHeight() / 2.f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

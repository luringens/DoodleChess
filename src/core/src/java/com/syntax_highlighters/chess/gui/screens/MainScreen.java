package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.UiBoard;

/**
 * Game main screen
 */
public class MainScreen implements Screen {
    private Game game;
    private Stage stage;
    UiBoard board;

    private Texture paper;
    private Image paperImage;
    private AssetManager assetManager;

    private SpriteBatch batch;

    public MainScreen(AssetManager manager) {
        assetManager = manager;
        paper = manager.get("paper.png", Texture.class);
        paperImage = new Image(paper);

        game = new Game(null, null);

        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        board = new UiBoard(assetManager, game);
        stage.addActor(paperImage);
        stage.addActor(board);
    }

    @Override
    public void render(float delta) {

        //Gdx.gl.glClearColor(0.25f, 0.3f, 0.35f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        assetManager.dispose();
    }
}

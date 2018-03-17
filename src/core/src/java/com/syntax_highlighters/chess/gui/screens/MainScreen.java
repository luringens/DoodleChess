package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;
import com.syntax_highlighters.chess.gui.UiBoard;

/**
 * Game main screen
 */
public class MainScreen implements Screen {

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Game game;

    private Stage stage;


    UiBoard board;

    public MainScreen() {

        game = new Game(false, false, 0);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        board = new UiBoard(game);
        stage.addActor(board);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, 0.3f, 0.35f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //camera.setToOrtho(false, width, height);
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
        batch.dispose();
    }
}

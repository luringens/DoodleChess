package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.UiBoard;
import com.syntax_highlighters.chess.gui.actors.Text;

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

    private Text turnText;
    private boolean waitingForAi = false;
    private boolean resizeFBO = false;

    FrameBuffer fbo;

    public MainScreen(AiDifficulty player1Difficulty, AiDifficulty player2Difficulty, AssetManager manager) {
        assetManager = manager;
        paper = manager.get("paper.png", Texture.class);
        paperImage = new Image(paper);

        game = new Game(player1Difficulty, player2Difficulty);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        board = new UiBoard(assetManager, game, stage);
        float size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - 50;
        board.setSize(size, size);
        stage.addActor(paperImage);
        stage.addActor(board);

        Texture texture = new Texture(Gdx.files.internal("segoeui.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont segoeUi = new BitmapFont(Gdx.files.internal("segoeui.fnt"), new TextureRegion(texture), false);


        turnText = new Text(segoeUi);
        turnText.setColor(0,0,0,1);
        stage.addActor(turnText);
        turnText.setText(game.nextPlayerIsWhite() ? "White's turn" : "Black's turn");
        fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        // TODO: Check if going to wait for ai
        if(!waitingForAi) {
            waitingForAi = true;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    game.PerformAIMove();
                    waitingForAi = false;
                }
            };
            if(resizeFBO)
            {
                resizeFBO = false;
                fbo.dispose();
                fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            }
            thread.start();
            fbo.begin();
            turnText.setText(game.nextPlayerIsWhite() ? "White's turn" : "Black's turn");
            stage.act(delta);
            stage.draw();
            fbo.end();
        }
        SpriteBatch batch = (SpriteBatch) stage.getBatch();
        batch.begin();
        batch.draw(fbo.getColorBufferTexture(), 0, 0,0, 0, fbo.getWidth(), fbo.getHeight(), 1, 1, 0, 0, 0, fbo.getWidth(), fbo.getHeight(), false, true);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        float size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - 100;
        size = Math.min(size, 1000);
        board.setSize(size, size);
        board.setPosition(width / 2.f - size / 2.f, height / 2.f - size / 2.f + 50);
        turnText.setCenter(width / 2.f, height / 2.f - size / 2.f);

        if(waitingForAi)
        {
            resizeFBO = true;
            return;
        }

        fbo.dispose();
        fbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
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

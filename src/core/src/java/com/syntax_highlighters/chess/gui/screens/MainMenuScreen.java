package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.gui.AssetLoader;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Image background;

    private Button playButton;

    private AssetManager assetManager;

    public MainMenuScreen(Game game, AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.load("background2.png", Texture.class);
        assetManager.load("play.png", Texture.class);
        assetManager.load("play_pressed.png", Texture.class);
        assetManager.finishLoading();

        stage = new Stage(new ScreenViewport());
        Texture tex = assetManager.get("background2.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(tex);
        background.setSize(800, 800);

        Drawable neutral = new SpriteDrawable(new Sprite(assetManager.get("play.png", Texture.class)));
        //Drawable down = new SpriteDrawable(new Sprite(assetManager.get("play_pressed.png", Texture.class)));
        playButton = new Button(neutral);
        playButton.setSize(200, 50);

        stage.addActor(background);
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);

        AssetLoader.LoadAssets(assetManager);
        assetManager.finishLoading();

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(assetManager));
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(playButton.isOver())
        {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
        }
        else
        {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        /*

        assetManager.finishLoading();
        //if(assetManager.update(1))
        {
            // TODO: Done, move to next screen
        }

        cam.update();
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        logo.draw(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.setColor(0.5f,0.7f,0.55f,1.f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float percentage = assetManager.getProgress();
        shapeRenderer.rect(-300, -20, 600.f * percentage, 10);
        shapeRenderer.end();*/
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        int min = Math.min(width, height);
        background.setSize(min, min);
        float x = 0;

        if(width > height)
        {
            x = width / 2.f - min / 2.f;
            background.setPosition(x, 0);
        }
        else
        {
            background.setPosition(0, height / 2.f - min / 2.f);
        }

        playButton.setPosition(x + 80, height/1.75f);



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

    }
}

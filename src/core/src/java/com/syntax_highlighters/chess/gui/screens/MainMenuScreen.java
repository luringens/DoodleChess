package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.actors.Button;

/***
 * Main menu screen
 */
public class MainMenuScreen extends AbstractScreen {

    private Stage stage;
    private Image background;

    private Button playButton;
    private Button scoreButton;

    /***
     * Constructor.
     *
     * Initializes resources used to render this screen.
     * @param game LibGdx game
     */
    public MainMenuScreen(ChessGame game) {
        super(game);

        AssetManager assetManager = game.getAssetManager();

        stage = new Stage(new ScreenViewport());

        Texture tex = assetManager.get("background2.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(tex);
        background.setSize(800, 800);

        playButton = new Button("Play", assetManager);
        playButton.setSize(250, 75);

        scoreButton = new Button("Leaderboards", assetManager);
        scoreButton.setSize(250, 75);

        stage.addActor(background);
        stage.addActor(playButton);
        stage.addActor(scoreButton);

        Gdx.input.setInputProcessor(stage);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SetupScreen(game));
            }
        });

        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SetupScoreScreen(game, assetManager));
            }
        });
    }

    /***
     * Renders the screen
     * @param delta time passed since last frame, in seconds
     */
    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    /***
     * Resize event.
     *
     * Used to correctly position the elements on screen and update the viewport size to support the new window size.
     * @param width new window width
     * @param height new window height
     */
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
        scoreButton.setPosition(x + 80, height/1.75f - 75);
    }

    /***
     * Disposes classes that needs disposing.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /***
     * unused
     */
    @Override
    public void show() {}

    /***
     * unused
     */
    @Override
    public void pause() {}

    /***
     * unused
     */
    @Override
    public void resume() {}

    /***
     * unused
     */
    @Override
    public void hide() {}
}

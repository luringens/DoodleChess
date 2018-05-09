package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.actors.Button;

/**
 * Main menu screen
 */
public class MainMenuScreen extends AbstractScreen {

    private final Image background;

    private final Button playButton;
    private final Button multiplayerButton;
    private final Button scoreButton;

    /**
     * Constructor.
     *
     * Initializes resources used to render this screen.
     * @param game LibGdx game
     */
    public MainMenuScreen(LibgdxChessGame game) {
        super(game);

        AssetManager assetManager = game.getAssetManager();

        Texture tex = assetManager.get("background2.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(tex);
        float size = Math.min(WORLDWIDTH, WORLDHEIGHT);
        background.setPosition(WORLDWIDTH / 2.0f - size / 2.f, WORLDHEIGHT / 2.f - size / 2.f);
        background.setSize(800, 800);

        stage.addActor(background);

        float x = WORLDWIDTH / 2.0f - 350.f;
        playButton = new Button.Builder("Local game", assetManager)
            .position(x, WORLDHEIGHT / 1.75f)
            .size(250, 75)
            .callback(() -> game.setScreen(new SetupScreen(game)))
            .stage(stage)
            .create();
        
        multiplayerButton = new Button.Builder("Multiplayer", assetManager)
            .position(x, WORLDHEIGHT / 1.75f - 75)
            .size(250, 75)
            .callback(() -> game.setScreen(new MultiplayerSetupScreen(game)))
            .stage(stage)
            .create();
        
        scoreButton = new Button.Builder("Leaderboards", assetManager)
            .position(x, WORLDHEIGHT / 1.75f - 150)
            .size(250, 75)
            .callback(() -> game.setScreen(new ScoreScreen(game)))
            .stage(stage)
            .create();
        
        Gdx.input.setInputProcessor(stage);

        Audio.themeMusic(assetManager,true);

    }

    /**
     * Resize event.
     *
     * Used to correctly position the elements on screen and update the viewport size to support the new window size.
     * @param width new window width
     * @param height new window height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}

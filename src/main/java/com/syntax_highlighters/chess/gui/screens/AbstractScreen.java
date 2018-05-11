package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.actors.DoodleActor;
import com.syntax_highlighters.chess.gui.actors.PaperBackground;

/**
 * Abstract screen implementing common functionality among different screens.
 */
public abstract class AbstractScreen implements Screen {
    private final LibgdxChessGame game;
    private boolean firstDraw = true;
    protected final Stage stage;
    protected final Image mute;
    private Boolean paused = false;
    private final static int N_DOODLES_PER_SCREEN = 7;
    private final static String[] DOODLES = new String[]{
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
        "Doodle/Horse.png"
    };

    protected static final float WORLDWIDTH = LibgdxChessGame.WORLDWIDTH;
    protected static final float WORLDHEIGHT = LibgdxChessGame.WORLDHEIGHT;

    /**
     * Create a screen based on a ChessGame.
     *
     * @param game The ChessGame instance of the gui
     */
    protected AbstractScreen(LibgdxChessGame game)
    {
        this(game, true);
    }

    /**
     * Create an AbstractScreen and also specify if doodles exist.
     *
     * @param game The ChessGame instance of the gui
     * @param createDoodles Whether or not to create doodles
     */
    protected AbstractScreen(LibgdxChessGame game, boolean createDoodles) {
        this.game = game;
        this.stage = new Stage(new FitViewport(WORLDWIDTH, WORLDHEIGHT), game.getBatch());
        PaperBackground paper = new PaperBackground(game.getAssetManager());
        paper.setSize(WORLDWIDTH, WORLDHEIGHT);
        this.stage.addActor(paper);
        if (createDoodles)
            addDoodles();

        AssetManager assetManager = game.getAssetManager();
        WobbleDrawable soundDrawable = new WobbleDrawable(assetManager.get("soundbutton.png", Texture.class), assetManager);
        WobbleDrawable muteDrawable = new WobbleDrawable(assetManager.get("mutebutton.png", Texture.class), assetManager);
        mute = new Image(soundDrawable);
        mute.setSize(100, 100);
        mute.setPosition(10, 10);
        stage.addActor(mute);

        mute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (paused)
                    mute.setDrawable(soundDrawable);
                else
                    mute.setDrawable(muteDrawable);
                Audio.themeMusic(assetManager, paused);
                paused = !paused;
            }
        });
    }

    /**
     * Helper method: add some random doodles to the stage.
     */
    private void addDoodles() {
        AssetManager assetManager = game.getAssetManager();

        // Draw some random doodles
        for (int i = 0; i < N_DOODLES_PER_SCREEN; i++) {
            // select a random doodle
            int index = (int)(Math.random()*DOODLES.length);
            Texture doodle = assetManager.get(DOODLES[index], Texture.class);
            float angle = (float)(Math.PI / 2.f * i / (N_DOODLES_PER_SCREEN));
            if(angle > Math.PI / 4.f) angle += Math.PI / 2.f;
            stage.addActor(new DoodleActor(doodle, angle));
        }
    }

    /**
     * Getter for the game object.
     *
     * @return The ChessGame instance of the gui
     */
    protected LibgdxChessGame getGame()
    {
        return game;
    }

    /**
     * Render all the actors on the stage.
     *
     * Subclasses must call this super method the last thing they do in their
     * render method.
     */
    @Override
    public void render(float delta) {
        if(firstDraw) {
            stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            firstDraw = false;
        }
        stage.act(delta);
        stage.draw();
    }

    /**
     * Disposes classes that needs disposing.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
    
    /**
     * Dummy implementation of unused method show.
     */
    @Override
    public void show() {}

    /**
     * Dummy implementation of unused method hide.
     */
    @Override
    public void hide() {}

    /**
     * Dummy implementation of unused method pause.
     */
    @Override
    public void pause() {}

    /**
     * Dummy implementation of unused method resume.
     */
    @Override
    public void resume() {}
}

package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.ChessGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.assets.AssetManager;
import com.syntax_highlighters.chess.gui.actors.DoodleActor;

/**
 * Abstract screen implementing common functionality among different screens.
 */
public abstract class AbstractScreen implements Screen {
    private final ChessGame game;
    protected final Stage stage;
    private final static int N_DOODLES_PER_SCREEN = 10;
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

    /**
     * Create a screen based on a ChessGame.
     *
     * @param game The ChessGame instance of the gui
     */
    protected AbstractScreen(ChessGame game)
    {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        AssetManager assetManager = game.getAssetManager();

        // Draw some random doodles
        for (int i = 0; i < N_DOODLES_PER_SCREEN; i++) {
            // select a random doodle
            int index = (int)(Math.random()*DOODLES.length);
            Texture doodle = assetManager.get(DOODLES[index], Texture.class);
            stage.addActor(new DoodleActor(doodle, (i+1)*(float)(2*Math.PI/N_DOODLES_PER_SCREEN)));
        }
    }

    /**
     * Getter for the game object.
     *
     * @return The ChessGame instance of the gui
     */
    protected Game getGame()
    {
        return game;
    }

//    /**
//     * Render all the actors on the stage.
//     *
//     * Subclasses must call this super method the last thing they do in their
//     * render method.
//     */
//    @Override
//    public void render(float delta) {
//        stage.act(delta);
//        stage.draw();
//    }
    
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

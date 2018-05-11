package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.syntax_highlighters.chess.account.AccountManager;
import com.syntax_highlighters.chess.gui.screens.LoadingScreen;
import com.syntax_highlighters.chess.gui.screens.MainMenuScreen;

/**
 * Wrapper for LibGdx Game class
 */
public class LibgdxChessGame extends Game {
	private AssetManager assetManager;
	private SpriteBatch background;
	private SpriteBatch batch;
	private AccountManager accountManager;
	private Texture table;
	public Skin skin;
	private boolean finishedLoading = false;

	private static final float aspectRatio = 1.6f;
	public static final float WORLDWIDTH = 800 * (aspectRatio > 1.0f ? aspectRatio : 1.0f);
	public static final float WORLDHEIGHT = 800.f / (aspectRatio <= 1.0f ? aspectRatio : 1.0f);

	/**
     * Game creation event, used to initialize resources
     */
	@Override
	public void create () {
		ShaderProgram.pedantic = false;
		assetManager = new AssetManager();
		AssetLoader.LoadAssets(assetManager);

		setScreen(new LoadingScreen(this));
	}

	public void finishedLoading() {
		finishedLoading = true;
		accountManager = new AccountManager(AssetLoader.getAccountPath());

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		skin = new Skin();
		skin.add("default-font", AssetLoader.GetDefaultFont(assetManager), BitmapFont.class);

		skin.add("wobbleButtonTemplate", new WobbleDrawable(assetManager.get("button_template.png"), assetManager), Drawable.class);
		skin.add("wobbleSelect", new WobbleDrawable(assetManager.get("selectBox.png"), assetManager), Drawable.class);

		skin.addRegions(atlas);
		skin.load(Gdx.files.internal("uiskin.json"));
		batch = new SpriteBatch();
		background = new SpriteBatch();

		assetManager.finishLoadingAsset("table.jpg");
		table = assetManager.get("table.jpg");
		table.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	/**
     * Overloaded setScreen method to recompute the background
     * @param screen the new screen
     */
	@Override
	public void setScreen(Screen screen) {
	    /*
	     * Dispose current screen
         * In LibGdx do you usually keep the screens and just switch between them,
         * we decided to recreate them when you have to switch since it would be easier
         * to make sure that everything is initialized correctly when switching screens.
         * This renders the "show" and "hide" methids in the screens useless here.
         */
	    if(this.getScreen() != null)
	        this.getScreen().dispose();
		super.setScreen(screen);
	}

    /**
     * Game's main rendering function
     * This will render the paper background first and then the current screen.
     */
	@Override
	public void render () {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(finishedLoading) {
			batch.setColor(1,1,1,1);

			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			background.begin();
			background.draw(table, 0, 0);
			background.end();

			resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		super.render();
	}

    /**
     * Disposes classes that needs disposing.
     */
	@Override
	public void dispose () {
	    assetManager.dispose();
	    batch.dispose();
	}

    /**
     * Libgdx manager for storing misc assets.
     * @return the AssetManager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

	/**
	 * Fetches the games account manager
	 *
	 * @return the account manager
	 */
	public AccountManager getAccountManager() {
		return accountManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}

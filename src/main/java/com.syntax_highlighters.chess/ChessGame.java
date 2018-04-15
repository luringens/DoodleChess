package com.syntax_highlighters.chess;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.screens.MainMenuScreen;

/**
 * Wrapper for LibGdx Game class
 */
public class ChessGame extends Game {
	private AssetManager assetManager;
	private SpriteBatch batch;
	private ShaderProgram noiseShader;
	private FrameBuffer paperBuffer;
	private FrameBuffer screenBuffer;
	private Texture paper;
	private AccountManager accountManager;
	public Skin skin;

	private final int ishW = 1920;
	private final int ishH = 1080;

	/**
     * Game creation event, used to initialize resources
     */
	@Override
	public void create () {
		assetManager = new AssetManager();
		AssetLoader.LoadAssets(assetManager);
		// TODO: Loading screen to prevent the black frames before load
		assetManager.finishLoading();

		accountManager = new AccountManager();
		accountManager.load(AssetLoader.getAccountPath());

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		skin = new Skin();
		skin.add("default-font", AssetLoader.GetDefaultFont(assetManager), BitmapFont.class);

		skin.add("wobbleButtonTemplate", new WobbleDrawable(assetManager.get("button_template.png"), assetManager), Drawable.class);
		skin.add("wobbleSelect", new WobbleDrawable(assetManager.get("selectBox.png"), assetManager), Drawable.class);

		skin.addRegions(atlas);
		skin.load(Gdx.files.internal("uiskin.json"));

        paper = assetManager.get("paper.png", Texture.class);

		batch = new SpriteBatch();
		noiseShader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/noise.frag"));
		paperBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, ishW, ishH, false);
		screenBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void resize(int width, int height) {

		super.resize(width, height);

		screenBuffer.dispose();
		screenBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

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
        recomputeBackground();
	}

    /**
     * Game's main rendering function
     * This will render the paper background first and then the current screen.
     */
	@Override
	public void render () {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw paper background
		batch.begin();
		batch.draw(paperBuffer.getColorBufferTexture(), 0,0, ishW, ishH, 0, 0, ishW, ishH, false, false);
		batch.end();

		super.render();
	}

    /**
     * Disposes classes that needs disposing.
     */
	@Override
	public void dispose () {
	    assetManager.dispose();
	    batch.dispose();
		noiseShader.dispose();
	    paperBuffer.dispose();
		screenBuffer.dispose();
	}

    /**
     * Libgdx manager for storing misc assets.
     * @return the AssetManager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * Will regenerate the wrinkles for the background using a custom built shader.
     * We generate it in an offscreen buffer to prevent lag from computing the image every single draw call.
     *
     * For more information on how the shader works, check out "assets/shaders/paper.frag"
     */
    private void recomputeBackground()
    {
		batch.setShader(noiseShader);
		paperBuffer.begin();
		batch.begin();
		noiseShader.setUniformf("u_offset", new Vector2((float)Math.random() * 100.f, (float)Math.random() * 100.f));
		batch.draw(paperBuffer.getColorBufferTexture(), 0, 0, ishW, ishH);
		batch.end();
		paperBuffer.end();
		batch.setShader(null);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    }

	/**
	 * Fetches the games account manager
	 *
	 * @return the account manager
	 */
	public AccountManager getAccountManager() {
		return accountManager;
	}
}

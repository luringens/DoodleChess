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
import com.syntax_highlighters.chess.AccountManager;
import com.syntax_highlighters.chess.gui.screens.MainMenuScreen;

/**
 * Wrapper for LibGdx Game class
 */
public class LibgdxChessGame extends Game {
	private AssetManager assetManager;
	private SpriteBatch background;
	private SpriteBatch batch;
	private ShaderProgram noiseShader;
	private FrameBuffer paperBuffer;
	private AccountManager accountManager;
	private Texture table;
	public Skin skin;

	public static final float aspectRatio = 1.6f;
	public static final float WORLDWIDTH = 800 * (aspectRatio > 1.0f ? aspectRatio : 1.0f);
	public static final float WORLDHEIGHT = 800.f / (aspectRatio <= 1.0f ? aspectRatio : 1.0f);

	/**
     * Game creation event, used to initialize resources
     */
	@Override
	public void create () {
		assetManager = new AssetManager();
		AssetLoader.LoadAssets(assetManager);
		// TODO: Loading screen to prevent the black frames before load
		assetManager.finishLoading();

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
		noiseShader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/noise.frag"));
		paperBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		table = assetManager.get("table.jpg");
		table.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
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
		batch.setColor(1,1,1,1);

		// Draw paper background
		// TODO: Paper to actor in AbstractScreen

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background.begin();
		background.draw(table, 0, 0);
		//background.draw(paperBuffer.getColorBufferTexture(), Gdx.graphics.getWidth() / 2.0f - WORLDWIDTH / 2.0f,0, WORLDWIDTH, WORLDHEIGHT);
		background.end();

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setShader(noiseShader);
		Matrix4 trans = batch.getTransformMatrix();
		batch.setTransformMatrix(new Matrix4());
		paperBuffer.begin();
		batch.begin();
		noiseShader.setUniformf("u_offset", new Vector2((float)Math.random() * 100.f, (float)Math.random() * 100.f));
		batch.draw(paperBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		paperBuffer.end();
		batch.setShader(null);
		batch.setTransformMatrix(trans);
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

	public SpriteBatch getBatch() {
		return batch;
	}
}

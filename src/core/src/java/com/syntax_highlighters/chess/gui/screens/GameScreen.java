package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.UiBoard;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.GameOverOverlay;
import com.syntax_highlighters.chess.gui.actors.Text;

import java.util.concurrent.Semaphore;

/**
 * Game main screen
 */
public class GameScreen extends AbstractScreen {
    private AssetManager assetManager;

    private Game game;
    private Stage stage;
    private UiBoard board;
    private Text turnText;
    private Button giveUp;

    private boolean waitingForAi = false;
    private boolean resizeFBO = false;
    private FrameBuffer gameBuffer;

    private boolean isGameOver = false;
    private GameOverOverlay gameOverOverlay;

    private Thread aiThread;
    private Semaphore aiLock = new Semaphore(1, true);

    private Account player1;
    private Account player2;
    private AiDifficulty ai1;
    private AiDifficulty ai2;

    private ChessGame chessGame;

    /***
     * Constructor.
     *
     * We have put the AI on a separate thread to stop the window from becoming unresponsive while the AI is thinking.
     *
     * @param chessGame current ChessGame
     * @param player1Difficulty Difficulty of player 1 (null if no ai)
     * @param player2Difficulty Difficulty of player 2 (null if no ai)
     */
    public GameScreen(ChessGame chessGame, Account player1, Account player2, AiDifficulty player1Difficulty, AiDifficulty player2Difficulty) {
        super(chessGame);
        assetManager = chessGame.getAssetManager();
        this.chessGame = chessGame;
        this.game = new Game(player1Difficulty, player2Difficulty);

        this.player1 = player1;
        this.player2 = player2;
        this.ai1 = player1Difficulty;
        this.ai2 = player2Difficulty;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        board = new UiBoard(assetManager, this.game, stage);
        float size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - 50;
        board.setSize(size, size);
        stage.addActor(board);

        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);

        turnText = new Text(font);
        turnText.setColor(0,0,0,1);
        stage.addActor(turnText);
        turnText.setText(this.game.nextPlayerIsWhite() ? "White's turn" : "Black's turn");
        gameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        if(player1 == null && ai1 != null && player2 == null && ai2 != null)
            giveUp = new Button("Leave match", assetManager);
        else
            giveUp = new Button("Give up", assetManager);
        giveUp.setSize(200, 75);

        giveUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(player1 == null && ai1 != null && player2 == null && ai2 != null)
                {
                    try {
                        aiLock.acquire(1);
                        isGameOver = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally
                    {
                        aiLock.release(1);
                    }

                    try {
                        aiThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chessGame.setScreen(new MainMenuScreen(chessGame));
                    return;
                }
                gameOver(game.nextPlayerIsWhite() ? 1 : -1);
            }
        });
        stage.addActor(giveUp);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameOverOverlay = new GameOverOverlay(chessGame);
        gameOverOverlay.setVisible(false);
        stage.addActor(gameOverOverlay);

        aiThread = new Thread(() -> {
            while(true) {
                try {
                    try {
                        aiLock.acquire(1);
                        if (isGameOver) {
                            waitingForAi = false;
                            break;
                        }
                        if (waitingForAi && this.game.nextPlayerIsAI()) {
                            this.game.PerformAIMove();
                            waitingForAi = false;
                        }
                    }
                    // Ignore exception
                    catch(Exception e){}
                    finally{
                        aiLock.release(1);
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        aiThread.start();
    }

    private void gameOver(int winner)
    {
        isGameOver = true;

        if(winner == 1 )
        {
            if(player1 != null)
                player1.win();
            if(player2 != null)
                player2.loss();
        }
        else if(winner == -1)
        {
            if(player1 != null)
                player1.loss();
            if(player2 != null)
                player2.win();
        }
        else
        {
            if(player1 != null)
                player1.win();
            if(player2 != null)
                player2.win();
        }
        chessGame.getAccountManager().save(AssetLoader.getAccountPath());

        gameOverOverlay.updateText(winner, player1, player2, ai1, ai2);
        gameOverOverlay.setVisible(true);

    }

    /***
     * Render the screen
     *
     * Due to how the AI works do we have to draw it to an offscreen buffer before we let it think.
     * This is so that we can safely draw the game while the ai is thinking.
     * We also wait til the next draw call to resize the buffer the ai is thinking.
     * @param delta time passed since last frame, in seconds
     */
    @Override
    public void render(float delta) {

        SpriteBatch batch = (SpriteBatch) stage.getBatch();
        try {
            aiLock.acquire(1);

            giveUp.setVisible(!game.nextPlayerIsAI() || (player1 == null && player2 == null));

            if(!waitingForAi) {
                // Game over check
                if(game.isGameOver())
                {
                    if(!isGameOver)
                    {
                        gameOver(game.getWinner());
                    }
                    stage.act(delta);
                    stage.draw();
                    aiLock.release(1);
                    return;
                }

                // Tell ai thread to update
                waitingForAi = game.nextPlayerIsAI();

                // Resize buffer if necessary
                if (resizeFBO) {
                    resizeFBO = false;
                    gameBuffer.dispose();
                    gameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                }

                // Render to buffer
                gameBuffer.begin();
                Gdx.gl.glClearColor(0, 0, 0, 0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                turnText.setText(game.nextPlayerIsWhite() ? "White's turn" : "Black's turn");
                stage.act(delta);
                stage.draw();
                gameBuffer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            aiLock.release(1);
        }
        // Draw offscreen buffer
        batch.begin();
        batch.setColor(1,1,1,1);
        batch.draw(gameBuffer.getColorBufferTexture(), 0, 0,0, 0, gameBuffer.getWidth(),
                gameBuffer.getHeight(), 1, 1, 0, 0, 0,
                gameBuffer.getWidth(), gameBuffer.getHeight(), false, true);
        batch.end();
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
        float size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - 100;
        size = Math.min(size, 1000);
        board.setSize(size, size);
        board.setPosition(width / 2.f - size / 2.f, height / 2.f - size / 2.f + 25);
        turnText.setCenter(width / 2.f, height / 2.f - size / 2.f - 10.f);

        giveUp.setPosition(width / 2.f + size/2.f + 20.f - giveUp.getWidth(), height / 2.f - size / 2.f - giveUp.getHeight() / 1.5f);

        if(waitingForAi)
        {
            resizeFBO = true;
            return;
        }

        // Resizing the buffer
        gameBuffer.dispose();
        gameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
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
    public void hide() {}

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
}

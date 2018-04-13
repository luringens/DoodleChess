package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.UiBoard;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.GameOverOverlay;
import com.syntax_highlighters.chess.gui.actors.Text;
import com.syntax_highlighters.chess.PlayerAttributes;

import java.util.concurrent.Semaphore;

/**
 * Game main screen.
 */
public class GameScreen extends AbstractScreen {
    private final AssetManager assetManager;

    private final Game game;
    private final UiBoard board;
    private final Text turnText;
    private final Button giveUp;
    private final Button showResults;
    private final Image mute;

    private boolean isGameOver = false;
    private int winner = 0; // NOTE: do not consider this valid until isGameOver
    private final GameOverOverlay gameOverOverlay;

    private final Account player1;
    private final Account player2;
    private final AiDifficulty ai1;
    private final AiDifficulty ai2;
    private final Color player1Color;
    private final Color player2Color;
    private Boolean paused = false;
    private int xx = 1;

    private final ChessGame chessGame;

    /**
     * Constructor.
     * <p>
     * We have put the AI on a separate thread to stop the window from becoming unresponsive while the AI is thinking.
     *
     * @param chessGame current ChessGame
     * @param attrib1   Attributes for player 1 (account info, AI difficulty,
     *                  piece color)
     * @param attrib2   Attributes for player 2 (account info, AI difficulty,
     *                  piece color)
     */
    public GameScreen(ChessGame chessGame, PlayerAttributes attrib1, PlayerAttributes attrib2) {
        super(chessGame);

        assetManager = chessGame.getAssetManager();
        this.player1 = attrib1.getAccount();
        this.player2 = attrib2.getAccount();
        this.ai1 = attrib1.getAIDifficulty();
        this.ai2 = attrib2.getAIDifficulty();
        this.player1Color = attrib1.getColor();
        this.player2Color = attrib2.getColor();
        this.chessGame = chessGame;

        this.game = new Game(ai1, ai2);

        Gdx.input.setInputProcessor(stage);

        board = new UiBoard(assetManager, this.game, stage, this.player1Color, this.player2Color);
        float size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - 50;
        board.setSize(size, size);
        stage.addActor(board);

        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);

        turnText = new Text(font);
        turnText.setColor(0, 0, 0, 1);
        stage.addActor(turnText);
        turnText.setText(this.game.nextPlayerColor().isWhite() ? "White's turn" : "Black's turn");

        // display results button (initially invisible, but becomes visible when
        // game ends)
        showResults = new Button("Show results", assetManager);
        showResults.setSize(200, 75);
        showResults.setVisible(false); // do not show initially
        showResults.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameOverOverlay.setVisible(true);
            }
        });
        stage.addActor(showResults);

        mute = new Image(assetManager.get("soundbutton.png", Texture.class));
        mute.setSize(100, 100);
        stage.addActor(mute);

        mute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (paused)
                    mute.setDrawable(new SpriteDrawable(new Sprite(assetManager.get("soundbutton.png", Texture.class))));
                else
                    mute.setDrawable(new SpriteDrawable(new Sprite(assetManager.get("mutebutton.png", Texture.class))));
                Audio.themeMusic(assetManager, paused);
                paused = !paused;
            }
        });


        if (player1 == null && ai1 != null && player2 == null && ai2 != null)
            giveUp = new Button("Leave match", assetManager);
        else
            giveUp = new Button("Give up", assetManager);
        giveUp.setSize(200, 75);

        giveUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (player1 == null && ai1 != null && player2 == null && ai2 != null) {
                    isGameOver = true;
                    chessGame.setScreen(new MainMenuScreen(chessGame));
                    return;
                }
                gameOver(game.nextPlayerColor().isWhite() ? -1 : 1);
            }
        });
        stage.addActor(giveUp);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameOverOverlay = new GameOverOverlay(chessGame);
        gameOverOverlay.setVisible(false);
        stage.addActor(gameOverOverlay);
    }

    private void gameOver(int winner) {
        this.winner = winner;
        isGameOver = true;

        switch (winner) {
            case 1: // white player won
                if (player1 != null)
                    player1.win();
                if (player2 != null)
                    player2.loss();
                break;
            case -1: // black player won
                if (player1 != null)
                    player1.loss();
                if (player2 != null)
                    player2.win();
                break;
            default:
                if (player1 != null)
                    player1.win();
                if (player2 != null)
                    player2.win();
                break;
        }
        chessGame.getAccountManager().save(AssetLoader.getAccountPath());

        gameOverOverlay.updateText(winner, player1, player2, ai1, ai2);
        gameOverOverlay.setVisible(true);
        setTurnText();
    }

    /**
     * Render the screen
     * <p>
     * Due to how the AI works do we have to draw it to an offscreen buffer before we let it think.
     * This is so that we can safely draw the game while the ai is thinking.
     * We also wait til the next draw call to resize the buffer the ai is thinking.
     *
     * @param delta time passed since last frame, in seconds
     */
    @Override
    public void render(float delta) {

        // NOTE: when game is over, giveUp button is turned invisible, and
        // showResults button is turned visible. Otherwise, give up is
        // visible if applicable.
        giveUp.setVisible(!isGameOver &&
                (!game.nextPlayerIsAI() || (player1 == null && player2 == null)));
        showResults.setVisible(isGameOver);

        // Game over check
        if (game.isGameOver()) {
            if (!isGameOver) {
                gameOver(game.getWinner());
            }
            stage.act(delta);
            stage.draw();
            return;
        }
        if(game.nextPlayerIsAI())
            game.PerformAIMove();

        setTurnText();
        stage.act(delta);
        stage.draw();
    }

    /**
     * Helper method: set turn text according to whose turn it is, or to who the
     * winner is if the game is over, and center text.
     * <p>
     * Uses state variables "winner" and "isGameOver" in GameScreen.
     * <p>
     * NOTE: May want to update this later if we implement arbitrary color
     * choice
     */
    private void setTurnText() {
        if (!isGameOver) {
            turnText.setText(game.nextPlayerColor().isWhite() ? "White's turn" : "Black's turn");
        } else {
            turnText.setText(winner == 1 ? "White has won the game" :
                    winner == -1 ? "Black has won the game" :
                            "It's a draw!");
        }

        // this is modeled after the similar operation in the resize method
        // below
        int width = Gdx.graphics.getWidth();  // I think this is right
        int height = Gdx.graphics.getHeight();
        float size = Math.min(width, height) - 100;
        size = Math.min(size, 1000);
        turnText.setCenter(width / 2.f, height / 2.f - size / 2.f - 10.f);
    }

    /**
     * Resize event.
     * <p>
     * Used to correctly position the elements on screen and update the viewport size to support the new window size.
     *
     * @param width  new window width
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

        giveUp.setPosition(width / 2.f + size / 2.f + 20.f - giveUp.getWidth(),
                height / 2.f - size / 2.f - giveUp.getHeight() / 1.5f);

        showResults.setPosition(width / 2.f + size / 2.f + 20.f - showResults.getWidth(),
                height / 2.f - size / 2.f - showResults.getHeight() / 1.5f);
    }
}

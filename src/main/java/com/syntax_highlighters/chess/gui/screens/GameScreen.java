package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.entities.IAiPlayer;
import com.syntax_highlighters.chess.entities.MiniMaxAIPlayer;
import com.syntax_highlighters.chess.entities.IChessPiece;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.actors.BoardGroup;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.GameOverOverlay;
import com.syntax_highlighters.chess.gui.actors.ConfirmationOverlay;
import com.syntax_highlighters.chess.gui.actors.Text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Game main screen.
 */
public class GameScreen extends AbstractScreen {
    private final AssetManager assetManager;

    private final AbstractGame game;
    //private final UiBoard board;
    private final BoardGroup board;
    private final Text turnText;
    private final Button giveUp;
    private final Button getHelp;
    private final Button showResults;
    private final Image mute;
    private final List<String> history;
    private final ScrollPane historyPane;
    private final Table historyList;

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

    private final LibgdxChessGame chessGame;
    private com.syntax_highlighters.chess.entities.Color nextPlayerColor;

    /**
     * Constructor.
     * <p>
     * We have put the AI on a separate thread to stop the window from becoming unresponsive while the AI is thinking.
     *  @param chessGame current ChessGame
     * @param attrib1   Attributes for player 1 (account info, AI difficulty,
     *                  piece color)
     * @param attrib2   Attributes for player 2 (account info, AI difficulty,
     * @param randomBoard Whether or not to generate a random board or a regular one.
     */
    public GameScreen(LibgdxChessGame chessGame, PlayerAttributes attrib1, PlayerAttributes attrib2, boolean randomBoard) {
        super(chessGame, false);

        assetManager = chessGame.getAssetManager();
        this.player1 = attrib1.getAccount();
        this.player2 = attrib2.getAccount();
        this.ai1 = attrib1.getAIDifficulty();
        this.ai2 = attrib2.getAIDifficulty();
        this.player1Color = attrib1.getColor();
        this.player2Color = attrib2.getColor();
        this.chessGame = chessGame;

        this.game = new SjadamGame();
        if(randomBoard)
            // Do 15-25 random moves
            this.game.getBoard().setupPracticeGame((int)(Math.random() * 5) + 20);

        this.nextPlayerColor = this.game.nextPlayerColor().opponentColor();

        Gdx.input.setInputProcessor(stage);

        board = new BoardGroup(this.game, this.player2Color, this.player1Color, assetManager);
        float size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - 50;
        board.setSize(size, size);
        stage.addActor(board);

        // TODO: Move style stuff to a separate class
        // TODO: Fix clipping by creating custom list object
        List.ListStyle style = new List.ListStyle();
        style.font = AssetLoader.GetDefaultFont(assetManager, 16);
        style.fontColorUnselected = Color.BLACK;
        style.fontColorSelected = Color.BLACK;
        style.selection = new SpriteDrawable(new Sprite(assetManager.get("transparent.png", Texture.class)));
        history = new List<>(style);
        history.setItems(game.getMoveHistory().toArray(new String[game.getMoveHistory().size()]));
        ScrollPane.ScrollPaneStyle sStyle = new ScrollPane.ScrollPaneStyle();

        //sStyle.background = new WobbleDrawable(assetManager.get("button_template.png"), assetManager, Color.BLACK);
        historyPane = new ScrollPane(history, sStyle);
        historyPane.setSize(200, 600);
        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);

        Text text = new Text(font);
        text.setColor(0,0,0,1);
        text.setText("History:");

        historyList = new Table();
        historyList.pad(20).padTop(30);
        historyList.add(text);
        historyList.row().spaceTop(15);
        historyList.background(new WobbleDrawable(assetManager.get("listBackground.png"), assetManager));
        historyList.setSize(200, 600);
        historyList.add(historyPane).expand().top();
        stage.addActor(historyList);



        turnText = new Text(font);
        turnText.setColor(0, 0, 0, 1);
        stage.addActor(turnText);
        turnText.setText(this.game.nextPlayerColor().isWhite() ? "White's turn" : "Black's turn");


        WobbleDrawable soundDrawable = new WobbleDrawable(assetManager.get("soundbutton.png", Texture.class), assetManager);
        WobbleDrawable muteDrawable = new WobbleDrawable(assetManager.get("mutebutton.png", Texture.class), assetManager);

        mute = new Image(soundDrawable);
        mute.setSize(100, 100);
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

        getHelp = new Button("Get hint", assetManager);
        getHelp.setSize(200, 75);
        getHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (!isGameOver && !game.nextPlayerIsAI()) {
                    // Suggest a simple move to the player.
                    IAiPlayer ai = new MiniMaxAIPlayer(nextPlayerColor, AiDifficulty.ShortSighted);
                    Move move = ai.GetMove(game.getBoard());
                    board.showSuggestion(move);
                }
            }
        });
        stage.addActor(getHelp);

        Button.Builder giveUpButtonBuilder;
        if (player1 == null && ai1 != null && player2 == null && ai2 != null)
            giveUpButtonBuilder = new Button.Builder("Leave match", assetManager)
                .callback(() -> chessGame.setScreen(new MainMenuScreen(chessGame)));
        else
            giveUpButtonBuilder = new Button.Builder("Give up", assetManager)
                .callback(() -> new ConfirmationOverlay.Builder(assetManager)
                        .title("Are you sure you want to give up?")
                        .confirmText("Give up")
                        .cancelText("Keep playing")
                        .visible(true)
                        .confirmCallback(() -> gameOver(game.nextPlayerColor().isWhite() ? -1 : 1))
                        .stage(stage)
                        .create());
        giveUp = giveUpButtonBuilder.size(200, 75).stage(stage).create();

        // Always added last!!!
        gameOverOverlay = new GameOverOverlay(chessGame);
        gameOverOverlay.setVisible(false);
        stage.addActor(gameOverOverlay);

        // display results button (initially invisible, but becomes visible when
        // game ends)
        showResults = new Button.Builder("Show results", assetManager)
                .size(200, 75)
                .callback(() -> gameOverOverlay.setVisible(true))
                .stage(stage)
                .visible(false)
                .create();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    private void gameOver(int winner) {
        this.winner = winner;
        isGameOver = true;
        board.unselectSelected();
        if (!game.isGameOver()) {
            game.forceGameEnd();
        }

        if(player1 != null && player2 != null) {

            switch (winner) {
                case 1: // white player won
                    chessGame.getAccountManager().updateRating(player1, player2);
                    break;
                case -1: // black player won
                    chessGame.getAccountManager().updateRating(player2, player1);
                    break;
                default:
                    chessGame.getAccountManager().updateRatingDraw(player1, player2);
                    break;
            }
        }
        chessGame.getAccountManager().save();

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

        java.util.List<String> moves = new ArrayList<>(game.getMoveHistory());
        if(moves.size() > history.getItems().size)
        {
            history.setItems(moves.toArray(new String[moves.size()]));
            historyList.layout();
            historyPane.setScrollPercentY(100);
        }

        // Has the board updated?
        if (game.nextPlayerColor() != nextPlayerColor) {
            nextPlayerColor = game.nextPlayerColor();
            board.clearSuggestion();

            // Game over check
            if (game.isGameOver()) {
                if (!isGameOver) {
                    gameOver(game.getWinner());
                }
                stage.act(delta);
                stage.draw();
                return;
            }

            // Do an AI turn if needed
            if (game.nextPlayerIsAI()) {
                game.PerformAIMove();
                // If the AI performed a promotion move, we need to add the
                // piece that was promoted to as an actor to the BoardGroup.
                Board b = game.getBoard();
                Move m = b.getLastMove();
                if (m instanceof PromotionMove) {
                    IChessPiece promoted = b.getAtPosition(m.getPosition());
                    board.addPiece(promoted);
                }
            }
        }

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
        float size = Math.min(width, height);
        size = Math.min(size, 1000);
        turnText.setCenter(width / 2.f, height / 2.f - size / 2.f + 40.f);
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
        board.setPosition(width / 2.f - size / 2.f - 40, height / 2.f - size / 2.f + 25);
        historyList.setPosition(width / 2.f + size / 2.f + 35,height / 2.f  - historyList.getHeight() / 2.f + 50);
        turnText.setCenter(width / 2.f, height / 2.f - size / 2.f - 10.f);

        getHelp.setPosition(width / 2.f - getHelp.getWidth()*1.5f - getHelp.getWidth() / 2.f,
                height / 2.f - size / 2.f - getHelp.getHeight() / 1.5f);

        giveUp.setPosition(width / 2.f + giveUp.getWidth() * 1.5f - getHelp.getWidth() / 2.f,
                height / 2.f - size / 2.f - giveUp.getHeight() / 1.5f);

        showResults.setPosition(width / 2.f + getHelp.getWidth()*1.5f - getHelp.getWidth() / 2.f,
                height / 2.f - size / 2.f - showResults.getHeight() / 1.5f);
    }
}

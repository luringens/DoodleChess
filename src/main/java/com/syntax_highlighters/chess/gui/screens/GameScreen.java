package com.syntax_highlighters.chess.gui.screens;

import java.util.ArrayList;

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
import com.syntax_highlighters.chess.game.AbstractGame;
import com.syntax_highlighters.chess.account.Account;
import com.syntax_highlighters.chess.game.BurningChess;
import com.syntax_highlighters.chess.game.ChessGame;
import com.syntax_highlighters.chess.move.Move;
import com.syntax_highlighters.chess.PlayerAttributes;
import com.syntax_highlighters.chess.move.PromotionMove;
import com.syntax_highlighters.chess.game.SjadamGame;
import com.syntax_highlighters.chess.ai.AiDifficulty;
import com.syntax_highlighters.chess.ai.IAiPlayer;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;
import com.syntax_highlighters.chess.ai.MiniMaxAIPlayer;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.actors.BoardGroup;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.ConfirmationOverlay;
import com.syntax_highlighters.chess.gui.actors.GameOverOverlay;
import com.syntax_highlighters.chess.gui.actors.Text;

/**
 * Game main screen.
 */
public class GameScreen extends AbstractScreen {
    final AssetManager assetManager;

    AbstractGame game;
    //private final UiBoard board;
    final BoardGroup board;
    final Text turnText;
    final Button giveUp;
    private final Button getHelp;
    private final Button showResults;
    private final List<String> history;
    private final ScrollPane historyPane;
    final Table historyList;

    private boolean isGameOver = false;
    private int winner = 0; // NOTE: do not consider this valid until isGameOver
    private final GameOverOverlay gameOverOverlay;

    private final Account player1;
    private final Account player2;
    private final AiDifficulty ai1;
    private final AiDifficulty ai2;
    private final Color player1Color;
    private final Color player2Color;

    private final LibgdxChessGame chessGame;
    private Button endTurnButton;

    /**
     * Set up a new gamescreen using a game instance and default settings.
     * @param chessGame current ChessGame.
     * @param game The game to use instead of chessgame.
     */
    GameScreen(LibgdxChessGame chessGame, AbstractGame game) {
        this(
            chessGame,
            null,
            new PlayerAttributes(new Account("White"), Color.WHITE),
            new PlayerAttributes(new Account("Black"), Color.BLACK),
            false,
            game
        );
    }

    /**
     * Sets up a new gamescreen.
     * @param chessGame current ChessGame.
     * @param selectedMode the selected game mode in string form.
     * @param attrib1   Attributes for player 1.
     * @param attrib2   Attributes for player 2.
     * @param randomBoard Whether or not to generate a random board or a regular one.
     */
    public GameScreen(LibgdxChessGame chessGame, String selectedMode,
            PlayerAttributes attrib1, PlayerAttributes attrib2, boolean randomBoard) {
        this(chessGame, selectedMode, attrib1, attrib2, randomBoard, null);
    }

    /**
     * Sets up a new gamescreen.
     * @param chessGame current ChessGame.
     * @param selectedMode the selected game mode in string form.
     * @param attrib1   Attributes for player 1.
     * @param attrib2   Attributes for player 2.
     * @param randomBoard Whether or not to generate a random board or a regular one.
     * @param newGame The game to use instead of chessgame.
     */
    private GameScreen(LibgdxChessGame chessGame,
                       String selectedMode,
                       PlayerAttributes attrib1,
                       PlayerAttributes attrib2,
                       boolean randomBoard,
                       AbstractGame newGame) {
        super(chessGame, false);
        // Can't define newGame and selectedmode at once - one has to be null!
        assert (selectedMode == null) != (newGame == null);

        assetManager = chessGame.getAssetManager();
        this.player1 = attrib1.getAccount();
        this.player2 = attrib2.getAccount();
        this.ai1 = attrib1.getAIDifficulty();
        this.ai2 = attrib2.getAIDifficulty();
        this.player1Color = attrib1.getColor();
        this.player2Color = attrib2.getColor();
        this.chessGame = chessGame;

        if (selectedMode != null) initChessGame(selectedMode, ai1, ai2, randomBoard);
        else this.game = newGame;

        Gdx.input.setInputProcessor(stage);

        board = new BoardGroup(this.game, this.player2Color, this.player1Color, assetManager);
        float boardPadding = 25.f;
        float size = Math.min(WORLDWIDTH, WORLDHEIGHT) - boardPadding*2.f;
        board.setSize(size, size);
        board.setPosition(boardPadding, boardPadding);
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
        historyList.setPosition(WORLDWIDTH / 4.f * 3.f - historyList.getWidth() / 2.f, WORLDHEIGHT / 2.f - historyList.getHeight() / 2.f + 50);
        stage.addActor(historyList);



        turnText = new Text(font);
        turnText.setColor(0, 0, 0, 1);
        stage.addActor(turnText);
        turnText.setText(this.game.nextPlayerColor().isWhite() ? "White's turn" : "Black's turn");



        getHelp = new Button("Get hint", assetManager);
        getHelp.setSize(200, 75);
        getHelp.setPosition(WORLDWIDTH / 4.f * 3.f - getHelp.getWidth() + 20.f, 25.f);
        getHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (!isGameOver && !game.nextPlayerIsAI()) {
                    // Suggest a simple move to the player.
                    IAiPlayer ai = new MiniMaxAIPlayer(AiDifficulty.ShortSighted);
                    Move move = ai.GetMove(game);
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
        giveUp = giveUpButtonBuilder.size(200, 75).position(WORLDWIDTH / 4.f * 3.f + 20.f, 25.f).stage(stage).create();


        endTurnButton = new Button.Builder("End turn", assetManager)
            .callback(() -> ((SjadamGame)game).endTurn())
            .size(200, 75)
            .stage(stage)
            .position(WORLDWIDTH / 4.f * 3.f - getHelp.getWidth() + 20.f, 25.f)
            .create();

        // Always added last!!!
        gameOverOverlay = new GameOverOverlay(chessGame);
        gameOverOverlay.setVisible(false);
        stage.addActor(gameOverOverlay);

        // display results button (initially invisible, but becomes visible when
        // game ends)
        showResults = new Button.Builder("Show results", assetManager)
            .position(WORLDWIDTH / 4.f * 3.f + 20.f, 25.f)
            .size(200, 75)
            .callback(() -> gameOverOverlay.setVisible(true))
            .stage(stage)
            .visible(false)
            .create();

        mute.setPosition(WORLDWIDTH - 110, 10.f);

    }

    private void initChessGame(String mode, AiDifficulty ai1, AiDifficulty ai2, boolean random) {
        switch (mode) {
            case "Regular Chess":
                this.game = new ChessGame(ai1, ai2);
                break;
            case "Sjadam":
                this.game = new SjadamGame();
                break;
            case "Fire Chess":
                this.game = new BurningChess(ai1, ai2);
                break;
            default:
                throw new IllegalArgumentException("Unknown game mode: " + mode);
        }
        if(random)
            // Do 15-25 random moves
            this.game.getBoard().setupPracticeGame((int)(Math.random() * 5) + 20);
    }

    void gameOver(int winner) {
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
        
        endTurnButton.setVisible(game instanceof SjadamGame && ((SjadamGame)game).hasJumped());
        
        getHelp.setVisible(game instanceof ChessGame);

        java.util.List<String> moves = new ArrayList<>(game.getMoveHistory());
        if(moves.size() > history.getItems().size)
        {
            history.setItems(moves.toArray(new String[moves.size()]));
            historyList.layout();
            historyPane.setScrollPercentY(100);
        }

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
            Move m = game.PerformAIMoveAsync();
            // If the AI performed a promotion move, we need to add the
            // piece that was promoted to as an actor to the BoardGroup.
            if (m != null) {
                if (m instanceof PromotionMove) {
                    IChessPiece promoted = game.getBoard().getAtPosition(m.getPosition());
                    board.addPiece(promoted);
                }
            }
        }

        setTurnText();

        super.render(delta);
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
    void setTurnText() {
        String p1Name = player1 != null ? player1.getName() : ai1 == null ? "Player 1" : "AI";
        String p2Name = player2 != null ? player2.getName() : ai2 == null ? "Player 2" : "AI";
        if (!isGameOver) {

            turnText.setText(game.nextPlayerColor().isWhite() ? p1Name + "'s turn" : p2Name + "'s turn");
        } else {
            turnText.setText(winner == 1 ? p1Name + " has won the game" :
                    winner == -1 ? p2Name + " has won the game" :
                            "It's a draw!");
        }

        turnText.setCenter(WORLDWIDTH / 4.f * 3.f, WORLDHEIGHT - 20.f);
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

    }
}

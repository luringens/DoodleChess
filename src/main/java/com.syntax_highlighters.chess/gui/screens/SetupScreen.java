package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Color;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.actors.*;
import com.syntax_highlighters.chess.PlayerAttributes;

import java.util.ArrayList;

/**
 * Screen allowing for setup options of the game of chess to be played.
 *
 * Allows account creation and selection, selection of AI level of either
 * player, return to main menu, and starting a new game according to the current
 * setup.
 */
public class SetupScreen extends AbstractScreen {

    private final Text title;
    private final Text playerNote;

    private final ChessPieceActor blackKing;
    private final ChessPieceActor whiteKing;

    private final Text sameAccountErrorMsg;

    private AiDifficulty player1Difficulty;
    private AiDifficulty player2Difficulty;
    private final AssetManager assetManager;

    private SelectBox<String> player1Title;
    private SelectBox<String> player2Title;

    private Button player1ColorShow;
    private Button player2ColorShow;
    private int selectingPlayer = -1;

    private final ArrayList<Button> player1Buttons = new ArrayList<>();
    private final ArrayList<Button> player2Buttons = new ArrayList<>();

    private final Button playButton;
    private final Button mainMenu;
    private final Button createAccount;

    private final PencilSelector selector;

    final float buttonBigWidth = 200;
    final float buttonBigHeight = 60;
    final float buttonSmallWidth = 175;
    final float buttonSmallHeight = 50;

    private Color player1Color = Color.WHITE;
    private Color player2Color = Color.BLACK;

    public SetupScreen(ChessGame game)
    {
        super(game);
        this.assetManager = game.getAssetManager();
        
        title = createText("Setup game", true, Color.BLACK);
        playerNote = createText("Note: Using the names Player 1 or Player 2 will not count to any score", false, Color.BLACK);
        sameAccountErrorMsg = createText("Error: Cannot use same account on both sides", false, Color.RED);
        sameAccountErrorMsg.setVisible(false); // display only if player tries to use same account on both sides

        //blackKing = new Image(this.assetManager.get("king_white.png", Texture.class));
        //whiteKing = new Image(this.assetManager.get("king_white.png", Texture.class));
        whiteKing = new ChessPieceActor(new ChessPieceKing(null, null), player1Color, null, assetManager);
        blackKing = new ChessPieceActor(new ChessPieceKing(null, null), player2Color, null, assetManager);
        whiteKing.setSize(120, 120);
        blackKing.setSize(120, 120);

        stage.addActor(blackKing);
        stage.addActor(whiteKing);

        addDifficultyList(game, -1);
        addDifficultyList(game, 1);

        AccountOverlay accountOverlay = new AccountOverlay(game, this, assetManager);
        accountOverlay.setVisible(false);
        
        playButton = createButton("Play", true, () -> startGame(game));

        mainMenu = createButton("Main menu", true, () -> game.setScreen(new MainMenuScreen(game)));
        createAccount = createButton("Create account", true, () -> accountOverlay.setVisible(true));

        // I wish I could simplify it more than this, but I can't justify
        // creating a createPencilSelector method or a separate
        // ColorChangeCallback interface.
        selector= new PencilSelector(assetManager);
        selector.setPosition(100, -200);
        selector.addListener(new PencilSelector.ColorSelectListener(){
            @Override
            public void colorSelected(PencilSelector.ColorSelectEvent event, Color color) {
                selectNewColor(selector, color);
            }
        });
        selector.hide(0.0f);
        selector.selectColor(player1Color);
        selector.selectColor(player2Color);

        player1ColorShow = createButton("Choose color", false, () -> {selector.show(1.0f); selectingPlayer = 0;});
        player2ColorShow = createButton("Choose color", false, () -> {selector.show(1.0f); selectingPlayer = 1;});

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        // add this last
        stage.addActor(accountOverlay);
        stage.addActor(selector);
    }

    public void updateAccountLists(ChessGame game)
    {
        ArrayList<String> accounts = new ArrayList<>();
        accounts.add("Player 1");
        for(Account acc : game.getAccountManager().getAll())
        {
            accounts.add(acc.getName());
        }
        player1Title.setItems(accounts.stream().map(String::toString).toArray(String[]::new));
        
        // Reuse account list for right-hand side, but exchange dummy account
        // "Player 1" with "Player 2"
        accounts.set(0,"Player 2");
        player2Title.setItems(accounts.stream().map(String::toString).toArray(String[]::new));

    }

    /* HELPER METHODS */

    private void resetbuttonList(int player)
    {
        for (int i = 0; i < player1Buttons.size(); i++) {
            getAIDifficultySettingButton(player, i).setSelected(false);
        }
    }

    private void setDifficulty(int player, int difficulty)
    {
        if(player == -1)
            player1Difficulty = difficulty == -1 ? null : AiDifficulty.values()[difficulty];
        else
            player2Difficulty = difficulty == -1 ? null : AiDifficulty.values()[difficulty];
    }

    private void addDifficultyList(ChessGame game, int player)
    {
        SelectBox<String> box = new SelectBox<>(game.skin);

        ArrayList<String> accounts = new ArrayList<>();
        accounts.add("Player" + (player == -1 ? "1" : "2"));
        for(Account acc : game.getAccountManager().getAll())
        {
            accounts.add(acc.getName());
        }

        box.setItems(accounts.stream().map(String::toString).toArray(String[]::new));
        box.setSelected("Player" + (player == -1 ? "1" : "2"));
        box.setAlignment(Align.center);
        box.setSize(200,  buttonBigHeight);
        stage.addActor(box);
        if(player == -1)
            player1Title = box;
        else
            player2Title = box;

        String[] labels = new String[]{"No Ai", "Easy", "Medium", "Hard"};
        for(int i = 0; i < labels.length; ++i)
        {
            final int difficulty = i-1;
            Button button = createButton(labels[i], false, () -> {
                resetbuttonList(player);
                getAIDifficultySettingButton(player, difficulty+1).setSelected(true);
                if(difficulty >= -1 && difficulty < AiDifficulty.values().length)
                    setDifficulty(player, difficulty);

                if(player == -1)
                    player1Title.setVisible(difficulty == -1);
                else
                    player2Title.setVisible(difficulty == -1);
            });

            if(i == 0)
            {
                button.setSelected(true);
                setDifficulty(player, -1);
            }

            if(player == -1)
                player1Buttons.add(button);
            else
                player2Buttons.add(button);
        }
    }

    /**
     * Helper method: Retrieve the AI difficulty selection button of the given
     * player at the given position.
     *
     * @param player -1 for player 1, or 1 for player 2
     * @param index The index of the button to select - in range [0,4)
     *
     * @return The selected button
     */
    private Button getAIDifficultySettingButton(int player, int index) {
        if (!(player == -1 || player == 1)) {
            throw new IllegalArgumentException("player == -1 || player == 1");
        }
        if (!(0 <= index && index < 4)) {
            throw new IllegalArgumentException("0 <= index && index < 4");
        }

        return (player == -1 ? player1Buttons : player2Buttons).get(index);

    }

    /**
     * Helper method: create a PlayerAttributes instance based on the Account,
     * AiDifficulty and Color passed as arguments.
     *
     * Chooses the correct constructor based on which of the arguments are null.
     *
     * @param acc The selected Account, if selected, or null
     * @param diff The selected AI difficulty, if selected, or null
     * @param color The selected Color
     *
     * @return A PlayerAttributes object which contains the Account information
     * (if there is no AI information), or otherwise contains the AI difficulty
     * information
     */
    private PlayerAttributes createAttributes(Account acc, AiDifficulty diff, Color color) {
        if (diff == null)
            return new PlayerAttributes(acc, color);
        return new PlayerAttributes(diff, color);
    }

    /**
     * Helper method: Create a Text object with the given text and color, and
     * add it to the stage.
     *
     * This method is used increase readability and maintainability in the setup
     * process, where every line of text created requires 4 lines of code. Using
     * this method, the same can be accomplished in one.
     *
     * @param text The text content
     * @param color The color of the text
     *
     * @return The newly created object
     */
    private Text createText(String text, boolean big, Color color) {
        Text t = new Text(AssetLoader.GetDefaultFont(assetManager, big));
        t.setText(text);
        t.setColor(color);
        stage.addActor(t);
        return t;
    }
    
    /**
     * Helper method: Create a button with the given text and click callback,
     * and add it to the stage.
     *
     * NOTE: Does not work if you need to know the parameters of the clicked
     * method, instead of simply the fact that the button was clicked.
     *
     * @param text The button text
     * @param action The callback action
     *
     * @return The newly created button
     */
    private Button createButton(String text, boolean big, Button.Callback action) {
        return new Button.Builder(text, assetManager)
            .stage(stage)
            .callback(action)
            .size(big ? buttonBigWidth : buttonSmallWidth, big ? buttonBigHeight : buttonSmallHeight)
            .create();
    }
    
    /**
     * Helper method: Determine if the account selection is invalid.
     *
     * True if either string is null or empty, or if they are equal to each
     * other (you cannot play with the same account on both sides).
     *
     * @param acc1 The first account name
     * @param acc2 The second account name
     *
     * @return true if the account selection is invalid, false otherwise
     */
    private boolean invalidAccountSelection(String acc1, String acc2) {
        return acc1 == null || acc1.isEmpty() || acc2 == null || acc2.isEmpty() || acc1.equals(acc2);
    }

    /**
     * Helper method: Exchange the selecting player's color with the given
     * color, and return the old color.
     *
     * @param color The color to change to
     * @return The new selected color
     */
    private Color swapColor(Color color) {
        Color ret = null;
        if (selectingPlayer == 0) {
            ret = player1Color;
            player1Color = color;
        }
        else if (selectingPlayer == 1) {
            ret = player2Color;
            player2Color = color;
        }
        return ret;
    }

    /**
     * Helper method: Start the game.
     *
     * NOTE: The ChessGame parameter passed is there because although
     * AbstractScreen has a ChessGame game field, that field is private and
     * cannot be accessed in subclasses. The reason it could be accessed in the
     * constructor is because it's there present as a local variable passed to
     * the constructor, and thus accessible from the anonymous class created.
     * This is perhaps not ideal - perhaps the game field should be protected in
     * AbstactScreen instead - but this is a workaround designed not to disturb
     * legacy code before I fully know the implications.
     *
     * May be refactored by anyone who wants to touch this hornet's nest.
     *
     * @param game Local variable instance parameter
     */
    private void startGame(ChessGame game) {
        String selected1 = player1Title.getSelected();
        String selected2 = player2Title.getSelected();

        if(invalidAccountSelection(selected1, selected2))
        {
            if (selected1 != null && selected1.equals(selected2))
                sameAccountErrorMsg.setVisible(true);
            return;
        }
        AccountManager manager = game.getAccountManager();
        Account player1 = manager.getAccount(selected1);
        Account player2 = manager.getAccount(selected2);

        // Create player attribute objects
        PlayerAttributes attrib1 = createAttributes(player1, player1Difficulty, player1Color);
        PlayerAttributes attrib2 = createAttributes(player2, player2Difficulty, player2Color);
        game.setScreen(new GameScreen(game, attrib1, attrib2));
    }

    /**
     * Helper method: select new color.
     *
     * Handles the state changes and delay actions.
     *
     * @param color The new color
     */
    private void selectNewColor(PencilSelector selector, Color color) {
        selector.hide(1.0f);
        final Color currentColor = swapColor(color); // returns previously selected color
        DelayAction delay = new DelayAction();
        delay.setDuration(1.0f);
        RunnableAction runnable = new RunnableAction();
        runnable.setRunnable(() -> {
            selector.releaseColor(currentColor);
            selector.selectColor(player1Color);
            whiteKing.setColor(player1Color);
            blackKing.setColor(player2Color);

        });
        selector.addAction(new SequenceAction(delay, runnable));

        selectingPlayer = -1;
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        title.setCenter(width / 2.f, height / 2.f + 400.f - buttonBigHeight*1.5f);

        float column1 = width/2.f - 800/4.f;
        float column2 = width/2.f + 800/4.f;

        selector.setPosition(width/2.f - selector.getWidth()/2.f, selector.getY());

        float y = height / 2.f + 400 - buttonBigHeight * 1.5f - blackKing.getHeight();
        whiteKing.setPosition(column1 - whiteKing.getWidth() / 2.f, y);
        blackKing.setPosition(column2 - blackKing.getWidth() / 2.f, y);

        y -= 80;

        player1Title.setPosition(column1 - buttonBigWidth / 2.f, y);
        player2Title.setPosition(column2 - buttonBigWidth / 2.f, y);

        y -= buttonBigHeight;

        player1ColorShow.setPosition(column1 - buttonSmallWidth / 2.f, y);
        player2ColorShow.setPosition(column2 - buttonSmallWidth / 2.f, y);

        y -= buttonBigHeight;
        float tempY = y;
        for(int i = 0; i < player1Buttons.size(); ++i)
        {
            Button button = player1Buttons.get(i);
            button.setPosition(column1 - buttonSmallWidth / 2.f, y);
            y -= buttonSmallHeight;
        }
        y = tempY;
        for(int i = 0; i < player2Buttons.size(); ++i)
        {
            Button button = player2Buttons.get(i);
            button.setPosition(column2 - buttonSmallWidth / 2.f, y);
            y -= buttonSmallHeight;
        }

        y -= buttonBigHeight / 2.f;

        float cw = width / 2.f - buttonBigWidth / 2.f;

        playButton.setPosition(column2 - buttonBigWidth/2.f, y);

        mainMenu.setPosition(column1 - buttonBigWidth / 2.f, y);
        createAccount.setPosition(cw, y);
        playerNote.setCenter(width / 2.f, y - 20.f);
        sameAccountErrorMsg.setCenter(width/2.f, y - 50.f);
    }

}

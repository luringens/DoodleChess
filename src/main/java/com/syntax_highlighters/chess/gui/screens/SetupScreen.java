package com.syntax_highlighters.chess.gui.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.Align;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import com.syntax_highlighters.chess.PlayerAttributes;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.actors.AccountOverlay;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.CheckButton;
import com.syntax_highlighters.chess.gui.actors.ChessPieceActor;
import com.syntax_highlighters.chess.gui.actors.PencilSelector;
import com.syntax_highlighters.chess.gui.actors.RadioGroup;
import com.syntax_highlighters.chess.gui.actors.Text;

/**
 * Screen allowing for setup options of the game of chess to be played.
 *
 * Allows account creation and selection, selection of AI level of either
 * player, return to main menu, and starting a new game according to the current
 * setup.
 */
public class SetupScreen extends AbstractScreen {

    private final Text title;      // title of setup screen
    private final Text playerNote; // not sure

    // image indicating the color of each player
    private final ChessPieceActor blackKing;
    private final ChessPieceActor whiteKing;

    // displayed if the user tries to click play while selecting the same
    // account on both sides
    private final Text sameAccountErrorMsg;
    private final Text sjadamMultiplayerErrorMsg;

    private final AssetManager assetManager; // asset manager

    // dropdown menus for either account or AI difficulty selection
    private SelectBox<String> player1Title;
    private SelectBox<String> player2Title;
    
    private SelectBox<String> gameModes;
    

    // buttons for displaying the color picker dialog for each player
    private Button player1ColorShow;
    private Button player2ColorShow;
    private int selectingPlayer = -1;

    private final Button playButton;    // start game
    private final Button mainMenu;      // return to main menu
    private final Button createAccount; // show account creation dialog

    private final PencilSelector selector; // color picker

    // constants
    final float buttonBigWidth = 200;
    final float buttonBigHeight = 60;
    final float buttonSmallWidth = 175;
    final float buttonSmallHeight = 50;

    // logical color variables of the two players
    private Color player1Color = Color.WHITE;
    private Color player2Color = Color.BLACK;

    // whether or not to randomize the board
    private boolean randomBoard = false;

    // radio button groups for each player indicating whether the player is a
    // human player or AI player
    private RadioGroup rb1;
    private RadioGroup rb2;

    private CheckButton randomCheckButton;

    public SetupScreen(LibgdxChessGame game)
    {
        super(game);
        this.assetManager = game.getAssetManager();
        
        title = createText("Setup game", true, Color.BLACK);
        title.setCenter(WORLDWIDTH/2,WORLDHEIGHT-title.getHeight()-10);
        playerNote = createText("Note: Using the names Player 1 or Player 2 will not count to any score", false, Color.BLACK);
        sameAccountErrorMsg = createText("Error: Cannot use same account on both sides", false, Color.RED);
        sjadamMultiplayerErrorMsg = createText("Error: this mode only supports human vs. human gameplay", false, Color.RED);
        sjadamMultiplayerErrorMsg.setVisible(false);
        sameAccountErrorMsg.setVisible(false); // display only if player tries to use same account on both sides

        float column1 = WORLDWIDTH / 2.f - buttonBigWidth;
        float column2 = WORLDWIDTH / 2.f + buttonBigWidth;

        // display which player setup belongs to which player
        whiteKing = new ChessPieceActor(new ChessPieceKing(null, null), player1Color, null, assetManager);
        blackKing = new ChessPieceActor(new ChessPieceKing(null, null), player2Color, null, assetManager);
        whiteKing.setSize(120, 120);
        blackKing.setSize(120, 120);
        float y = WORLDHEIGHT / 2.f + 400 - buttonBigHeight * 1.5f - blackKing.getHeight();
        whiteKing.setPosition(column1 - whiteKing.getWidth() / 2.f, y);
        blackKing.setPosition(column2 - blackKing.getWidth() / 2.f, y);

        stage.addActor(blackKing);
        stage.addActor(whiteKing);

        // lists
        final List<String> accounts = game.getAccountManager().getAll().stream()
            .map(a->a.getName())
            .collect(Collectors.toList());
        final List<String> pl1Accounts = Stream.concat(Stream.of("Player1"), accounts.stream())
            .collect(Collectors.toList());
        final List<String> pl2Accounts = Stream.concat(Stream.of("Player2"), accounts.stream())
            .collect(Collectors.toList());
        final List<String> ais = Arrays.asList("Easy AI", "Medium AI", "Hard AI");
        final List<String> modes = Arrays.asList("Regular Chess", "Sjadam", "Fire Chess");
        
        // create select boxes
        final SelectBox<String> pl1 = createDropdownMenu(pl1Accounts, true, buttonBigWidth, buttonBigHeight);
        final SelectBox<String> pl2 = createDropdownMenu(pl2Accounts, true, buttonBigWidth, buttonBigHeight);
        final SelectBox<String> ai1 = createDropdownMenu(ais, false, buttonBigWidth, buttonBigHeight);
        final SelectBox<String> ai2 = createDropdownMenu(ais, false, buttonBigWidth, buttonBigHeight);
        // set which select boxes to show
        player1Title = pl1;
        player2Title = pl2;
        
        gameModes = createDropdownMenu(modes, true, buttonBigWidth + 40, buttonBigHeight); // set the game mode select box

        rb1 = new RadioGroup(assetManager, false);
        rb2 = new RadioGroup(assetManager, false);

        rb1.addButton("Human player", selected -> {
            if (selected) player1Title = swapDropdownMenu(pl1, ai1);
        });
        rb1.addButton("Machine player", selected -> {
            if (selected) player1Title = swapDropdownMenu(ai1, pl1);
        });
        rb1.setOnSelectionChangeCallback(
                () -> resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        
        rb2.addButton("Human player", selected -> {
            if (selected) player2Title = swapDropdownMenu(pl2, ai2);
        });
        rb2.addButton("Machine player", selected -> {
            if (selected) player2Title = swapDropdownMenu(ai2, pl2);
        });
        rb2.setOnSelectionChangeCallback(
                () -> resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        y -= rb1.getHeight();
        rb1.setPosition(column1 - buttonBigWidth / 2.f, y);
        rb2.setPosition(column2 - buttonBigWidth / 2.f, y);

        y -= 80;
        player1Title.setPosition(column1 - buttonBigWidth / 2.f, y);
        player2Title.setPosition(column2 - buttonBigWidth / 2.f, y);


        stage.addActor(rb1);
        stage.addActor(rb2);

        // random game checkbutton
        randomCheckButton = new CheckButton(assetManager, "Random starting positions",
                Color.BLACK);
        randomCheckButton.setOnSelectionChangedCallback(selected -> randomBoard = selected);
        stage.addActor(randomCheckButton);

        AccountOverlay accountOverlay = new AccountOverlay(game, this, assetManager);
        accountOverlay.setVisible(false);
        
        playButton = createButton("Play", true, () -> startGame(game));

        mainMenu = createButton("Main menu", true, () -> game.setScreen(new MainMenuScreen(game)));
        createAccount = createButton("Create account", true, () -> accountOverlay.setVisible(true));

        // I wish I could simplify it more than this, but I can't justify
        // creating a createPencilSelector method or a separate
        // ColorChangeCallback interface.
        selector= new PencilSelector(assetManager);
        selector.setPosition(0, -200);
        selector.addListener(new PencilSelector.ColorSelectListener(){
            @Override
            public void colorSelected(PencilSelector.ColorSelectEvent event, Color color) {
                selectNewColor(selector, color);
            }
        });
        selector.selectColor(player1Color);
        selector.selectColor(player2Color);
        selector.setPosition(Gdx.graphics.getWidth()/2.f - selector.getWidth()/2.f, selector.getY());
        selector.hide(0.0f);

        player1ColorShow = createButton("Choose color", false, () -> {selector.show(1.0f); selectingPlayer = 0;});
        player2ColorShow = createButton("Choose color", false, () -> {selector.show(1.0f); selectingPlayer = 1;});

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        // add this last
        stage.addActor(accountOverlay);
        stage.addActor(selector);

        y -= buttonBigHeight;

        player1ColorShow.setPosition(column1 - buttonSmallWidth / 2.f, y);
        player2ColorShow.setPosition(column2 - buttonSmallWidth / 2.f, y);


        y = 275.f;

        gameModes.setPosition(WORLDWIDTH / 2.f - gameModes.getWidth() / 2.f, y);
        y -= gameModes.getHeight() + 15.f;
        randomCheckButton.setPosition(WORLDWIDTH / 2.f - randomCheckButton.getWidth() / 2.f,
                y + randomCheckButton.getHeight());

        y = 100.f;

        float cw = WORLDWIDTH / 2.f - buttonBigWidth / 2.f;
        playButton.setPosition(column2 - buttonBigWidth / 2.f, y);

        mainMenu.setPosition(column1 - buttonBigWidth / 2.f, y);
        createAccount.setPosition(cw, y);
        playerNote.setCenter(WORLDWIDTH / 2.f, y - 20.f);
        sameAccountErrorMsg.setCenter(WORLDWIDTH / 2.f, y - 50.f);
        sjadamMultiplayerErrorMsg.setCenter(WORLDWIDTH / 2.f, y - 70.f);

    }

    private SelectBox<String> swapDropdownMenu(SelectBox<String> show, SelectBox<String> hide) {
        show.setVisible(true);
        hide.setVisible(false);
        return show;
    }

    public SetupScreen(LibgdxChessGame game, boolean randomBoard) {
        this(game);
        this.randomBoard = randomBoard;
    }

    public void updateAccountLists(LibgdxChessGame game)
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

    /**
     * Helper method: Create a dropdown menu with the given elements and add it
     * to the stage.
     *
     * @param options Which options are visible in the dropdown menu
     * @param visible Whether or not this drop down menu should be shown or not
     *
     * @return The newly created dropdown menu
     */
    private SelectBox<String> createDropdownMenu(List<String> options, boolean visible, float width, float height) {
        SelectBox<String> box = new SelectBox<>(getGame().skin);
        box.setItems(options.stream().map(String::toString).toArray(String[]::new));
        box.setSelected(options.get(0));
        box.setAlignment(Align.center);
        box.setSize(width, height);
        box.setVisible(visible);
        stage.addActor(box);
        return box;
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
    private void startGame(LibgdxChessGame game) {
        String selected1 = player1Title.getSelected();
        String selected2 = player2Title.getSelected();
        String selectedMode = gameModes.getSelected();

        int si1 = rb1.getSelectedIndex();
        int si2 = rb2.getSelectedIndex();
        sjadamMultiplayerErrorMsg.setVisible(false);
        sameAccountErrorMsg.setVisible(false);
        if(selectedMode.equals("Sjadam") || selectedMode.equals("Fire Chess")){
            if(si1 == 1 || si2 == 1) {
                sjadamMultiplayerErrorMsg.setVisible(true);
                return;
            } 
        }
        if(si1 == 0 && invalidAccountSelection(selected1, selected2))
        {
            if (selected1 != null && selected1.equals(selected2))
                sameAccountErrorMsg.setVisible(true);
            return;
        }
        AccountManager manager = game.getAccountManager();
        Account player1 = si1 == 1 ? null : manager.getAccount(selected1);
        Account player2 = si2 == 1 ? null : manager.getAccount(selected2);
        
        AiDifficulty player1Difficulty = si1 == 0 ? null : getAiDifficulty(selected1);
        AiDifficulty player2Difficulty = si2 == 0 ? null : getAiDifficulty(selected2);

        // Create player attribute objects
        PlayerAttributes attrib1 = createAttributes(player1, player1Difficulty, player1Color);
        PlayerAttributes attrib2 = createAttributes(player2, player2Difficulty, player2Color);

        if(selectedMode.equals("Fire Chess"))
            game.setScreen(new BurningChessScreen(game, attrib1, attrib2, randomBoard));
        else
            game.setScreen(new GameScreen(game, selectedMode, attrib1, attrib2, randomBoard));
    }

    /**
     * Helper method: Retrieve the AI difficulty corresponding to the selected
     * option.
     *
     * @param aiLevel The string representing the AI difficulty level
     * @return The corresponding AiDifficulty
     */
    private AiDifficulty getAiDifficulty(String aiLevel) {
        switch(aiLevel) {
            case "Easy AI":
                return AiDifficulty.Easy;
            case "Medium AI":
                return AiDifficulty.Medium;
            case "Hard AI":
                return AiDifficulty.Hard;
            default:
                return null;
        }
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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

}

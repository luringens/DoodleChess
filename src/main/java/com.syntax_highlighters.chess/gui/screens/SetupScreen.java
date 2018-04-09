package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Color;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.actors.AccountOverlay;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.PencilSelector;
import com.syntax_highlighters.chess.gui.actors.Text;
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
    private final Text white;
    private final Text black;
    private final Text playerNote;

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

    final float buttonWidth = 200;
    final float buttonHeight = 75;

    private Color player1Color = Color.WHITE;
    private Color player2Color = Color.BLACK;

    public SetupScreen(ChessGame game)
    {
        super(game);
        this.assetManager = game.getAssetManager();
        
        title = createText("Select AI level", Color.BLACK);
        white = createText("White pieces:", Color.BLACK);
        black = createText("Black pieces:", Color.BLACK);
        playerNote = createText("Note: Using the names Player 1 or Player 2 will not count to any score", Color.BLACK);

        addDifficultyList(game, -1);
        addDifficultyList(game, 1);

        AccountOverlay accountOverlay = new AccountOverlay(game, this, assetManager);
        accountOverlay.setVisible(false);
        
        playButton = createButton("Play", () -> startGame(game));

        mainMenu = createButton("Main menu", () -> game.setScreen(new MainMenuScreen(game)));
        createAccount = createButton("Create account", () -> accountOverlay.setVisible(true));

        // TODO: refactor to prettify constructor
        PencilSelector selector= new PencilSelector(assetManager);
        selector.setPosition(100, -150);
        selector.addListener(new PencilSelector.ColorSelectListener(){
            @Override
            public void colorSelected(PencilSelector.ColorSelectEvent event, Color color) {
                selector.hide(1.0f);
                if(selectingPlayer == 0)
                {
                    final Color currentColor = player1Color;
                    DelayAction delay = new DelayAction();
                    delay.setDuration(1.0f);
                    player1Color = color;
                    RunnableAction runnable = new RunnableAction();
                    runnable.setRunnable(() -> {
                        selector.releaseColor(currentColor);
                        selector.selectColor(player1Color);

                    });
                    selector.addAction(new SequenceAction(delay, runnable));
                }
                else if(selectingPlayer == 1)
                {
                    final Color currentColor = player2Color;
                    DelayAction delay = new DelayAction();
                    delay.setDuration(1.0f);
                    player2Color = color;
                    RunnableAction runnable = new RunnableAction();
                    runnable.setRunnable(() -> {
                        selector.releaseColor(currentColor);
                        selector.selectColor(player2Color);

                    });
                    selector.addAction(new SequenceAction(delay, runnable));
                }

                selectingPlayer = -1;
            }
        });
        //selector.hide();
        stage.addActor(selector);
        selector.hide(0.0f);
        selector.selectColor(player1Color);
        selector.selectColor(player2Color);

        player1ColorShow = createButton("Choose color", () -> {selector.show(1.0f); selectingPlayer = 0;});
        player2ColorShow = createButton("Choose color", () -> {selector.show(1.0f); selectingPlayer = 1;});

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        // add this last
        stage.addActor(accountOverlay);
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
        if(player == -1)
        {
            for(Button button : player1Buttons)
                button.setSelected(false);
        }
        else
        {
            for(Button button : player2Buttons)
                button.setSelected(false);
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
        box.setSize(200,  45);
        stage.addActor(box);
        if(player == -1)
            player1Title = box;
        else
            player2Title = box;

        for(int i = 0; i < 4; ++i)
        {
            String text;
            switch(i)
            {
                case 0: text = "No Ai"; break;
                case 1: text = "Easy"; break;
                case 2: text = "Medium"; break;
                case 3: text = "Hard"; break;
                default: text = "Unknown"; break;
            }

            Button button = new Button(text, assetManager);

            button.setSize(buttonWidth, buttonHeight);

            final int difficulty = i-1;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    resetbuttonList(player);
                    button.setSelected(true);
                    if(difficulty >= -1 && difficulty < AiDifficulty.values().length)
                        setDifficulty(player, difficulty);

                    if(player == -1)
                    {
                        player1Title.setVisible(difficulty == -1);
                    }
                    else
                    {
                        player2Title.setVisible(difficulty == -1);
                    }
                }
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
            stage.addActor(button);
        }
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
    private Text createText(String text, Color color) {
        Text t = new Text(AssetLoader.GetDefaultFont(assetManager));
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
    private Button createButton(String text, Callback action) {
        Button b = new Button(text, assetManager);
        b.setSize(buttonWidth, buttonHeight);
        stage.addActor(b);
        b.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                action.perform();
            }
        });
        return b;
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
            // TODO: create error message(?)
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
     * Callback on button click.
     *
     * NOTE: Not aware of button click parameters, but enough for buttons, which
     * normally only need to know *that* a button was clicked, not anything
     * else.
     */
    private interface Callback {
        void perform();
    };

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        float centerW = width / 2.f - buttonWidth / 2.f;
        float centerH = height / 2.f - buttonHeight / 2.f;

        title.setCenter(width / 2.f, centerH + buttonHeight * 5);

        white.setCenter(centerW - buttonWidth + player1Title.getWidth() / 2.f, centerH + buttonHeight * 4.75f);
        black.setCenter(centerW + buttonWidth + player2Title.getWidth() / 2.f, centerH + buttonHeight * 4.75f);


        player1Title.setPosition(width / 2.f - buttonWidth - player1Title.getWidth() / 2.f,
                height / 2.f + buttonHeight * 3.5f - player1Title.getHeight() / 2.f);
        player2Title.setPosition(width / 2.f + buttonWidth - player2Title.getWidth() / 2.f,
                height / 2.f + buttonHeight * 3.5f - player1Title.getHeight() / 2.f);

        player1ColorShow.setPosition(width / 2.f - buttonWidth - player1Title.getWidth() / 2.f,
                height / 2.f + buttonHeight * 2.25f - player1Title.getHeight() / 2.f);
        player2ColorShow.setPosition(width / 2.f + buttonWidth - player2Title.getWidth() / 2.f,
                height / 2.f + buttonHeight * 2.25f - player1Title.getHeight() / 2.f);

        for(int i = 0; i < player1Buttons.size(); ++i)
        {
            Button button = player1Buttons.get(i);
            button.setPosition(centerW - buttonWidth, centerH - buttonHeight * (i-1.5f));
        }
        for(int i = 0; i < player2Buttons.size(); ++i)
        {
            Button button = player2Buttons.get(i);
            button.setPosition(centerW + buttonWidth, centerH - buttonHeight * (i-1.5f));
        }

        float bottomBarY = centerH - buttonHeight * 3;

        playButton.setPosition(centerW + buttonWidth + 10, bottomBarY);

        mainMenu.setPosition(centerW - buttonWidth - 10, bottomBarY);
        createAccount.setPosition(centerW, bottomBarY);
        playerNote.setCenter(width / 2.f, bottomBarY - 10.f);
    }
}

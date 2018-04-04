package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.actors.AccountOverlay;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.Text;

import java.util.ArrayList;

/**
 * Screen allowing for setup options of the game of chess to be played.
 *
 * Allows account creation and selection, selection of AI level of either
 * player, return to main menu, and starting a new game according to the current
 * setup.
 */
public class SetupScreen extends AbstractScreen {

    private final Stage stage;

    private final Text title;
    private final Text white;
    private final Text black;
    private final Text playerNote;

    private AiDifficulty player1Difficulty;
    private AiDifficulty player2Difficulty;
    private final AssetManager assetManager;

    private SelectBox player1Title;
    private SelectBox player2Title;

    private final ArrayList<Button> player1Buttons = new ArrayList<>();
    private final ArrayList<Button> player2Buttons = new ArrayList<>();

    private final Button playButton;
    private final Button mainMenu;
    private final Button createAccount;

    final float buttonWidth = 200;
    final float buttonHeight = 75;

    public SetupScreen(ChessGame game)
    {
        super(game);
        this.assetManager = game.getAssetManager();
        stage = new Stage(new ScreenViewport());

        title = new Text(AssetLoader.GetDefaultFont(assetManager));
        title.setText("Select AI level");
        title.setColor(0,0,0,1);
        stage.addActor(title);

        white = new Text(AssetLoader.GetDefaultFont(assetManager));
        white.setText("White pieces:");
        white.setColor(0,0,0,1);
        stage.addActor(white);

        black = new Text(AssetLoader.GetDefaultFont(assetManager));
        black.setText("Black pieces:");
        black.setColor(0,0,0,1);
        stage.addActor(black);

        playerNote = new Text(AssetLoader.GetDefaultFont(assetManager));
        playerNote.setText("Note: Using the names Player 1 or Player 2 will not count to any score");
        playerNote.setColor(0,0,0,1);
        stage.addActor(playerNote);

        addDifficultyList(game, -1);
        addDifficultyList(game, 1);

        playButton = new Button("Play", assetManager);
        playButton.setSize(buttonWidth, buttonHeight);
        stage.addActor(playButton);

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                String selected1 = (String)player1Title.getSelected();
                String selected2 = (String)player2Title.getSelected();

                // TODO: Get account
                if(selected1 == null || selected1.isEmpty() || selected2 == null || selected2.isEmpty()
                        || selected1.equals(selected2))
                {
                    // Invalid account selection
                    return;
                }
                AccountManager manager = game.getAccountManager();
                Account player1 = manager.getAccount(selected1);
                Account player2 = manager.getAccount(selected2);
                game.setScreen(new GameScreen(game, player1, player2, player1Difficulty, player2Difficulty));
            }
        });

        mainMenu = new Button("Main menu", assetManager);
        mainMenu.setSize(buttonWidth, buttonHeight);
        stage.addActor(mainMenu);
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        AccountOverlay accountOverlay = new AccountOverlay(game, this, assetManager);
        accountOverlay.setVisible(false);


        createAccount = new Button("Create account", assetManager);
        createAccount.setSize(buttonWidth, buttonHeight);
        stage.addActor(createAccount);
        createAccount.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                accountOverlay.setVisible(true);
            }
        });

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
        player1Title.setItems(accounts.toArray());
        accounts.remove("Player 1");
        accounts.add(0,"Player 2");
        player2Title.setItems(accounts.toArray());

    }

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
        SelectBox box = new SelectBox(game.skin);

        ArrayList<String> accounts = new ArrayList<>();
        accounts.add("Player" + (player == -1 ? "1" : "2"));
        for(Account acc : game.getAccountManager().getAll())
        {
            accounts.add(acc.getName());
        }

        box.setItems(accounts.toArray());
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
            String text = null;
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

    @Override
    public void show() {

    }

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

        title.setCenter(width / 2.f, centerH + buttonHeight * 4);

        white.setCenter(centerW - buttonWidth + player1Title.getWidth() / 2.f, centerH + buttonHeight * 3.75f);
        black.setCenter(centerW + buttonWidth + player2Title.getWidth() / 2.f, centerH + buttonHeight * 3.75f);


        player1Title.setPosition(width / 2.f - buttonWidth - player1Title.getWidth() / 2.f,
                height / 2.f + buttonHeight * 2.5f - player1Title.getHeight() / 2.f);
        player2Title.setPosition(width / 2.f + buttonWidth - player2Title.getWidth() / 2.f,
                height / 2.f + buttonHeight * 2.5f - player1Title.getHeight() / 2.f);

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

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

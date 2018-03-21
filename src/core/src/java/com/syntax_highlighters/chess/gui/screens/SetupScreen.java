package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.actors.AccountOverlay;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.Text;

import java.util.ArrayList;

public class SetupScreen extends AbstractScreen {

    private Stage stage;

    private Text title;

    private AiDifficulty player1Difficulty;
    private AiDifficulty player2Difficulty;
    private AssetManager assetManager;

    private Text player1Title;
    private Text player2Title;

    private ArrayList<Button> player1Buttons = new ArrayList<>();
    private ArrayList<Button> player2Buttons = new ArrayList<>();

    private Button playButton;
    private Button mainMenu;
    private Button createAccount;

    float buttonWidth = 200;
    float buttonHeight = 75;

    public SetupScreen(ChessGame game)
    {
        super(game);
        this.assetManager = game.getAssetManager();
        stage = new Stage(new ScreenViewport());

        title = new Text(AssetLoader.GetDefaultFont(assetManager));
        title.setText("Select AI level");
        title.setColor(0,0,0,1);
        stage.addActor(title);

        addDifficultyList(-1);
        addDifficultyList(1);

        playButton = new Button("Play", assetManager);
        playButton.setSize(buttonWidth, buttonHeight);
        stage.addActor(playButton);

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new GameScreen(game, player1Difficulty, player2Difficulty));
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

        AccountOverlay accountOverlay = new AccountOverlay(assetManager);
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

    private void addDifficultyList(int player)
    {
        Text title = new Text(AssetLoader.GetDefaultFont(assetManager));
        title.setText("Player" + (player == -1 ? "1" : "2"));
        title.setColor(0,0,0,1);
        stage.addActor(title);

        if(player == -1)
            player1Title = title;
        else
            player2Title = title;

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

        player1Title.setCenter(width / 2.f - buttonWidth, height / 2.f + buttonHeight * 2.75f);
        player2Title.setCenter(width / 2.f + buttonWidth, height / 2.f + buttonHeight * 2.75f);

        for(int i = 0; i < player1Buttons.size(); ++i)
        {
            Button button = player1Buttons.get(i);
            button.setPosition(centerW - buttonWidth, centerH - buttonHeight * (i-2));
        }
        for(int i = 0; i < player2Buttons.size(); ++i)
        {
            Button button = player2Buttons.get(i);
            button.setPosition(centerW + buttonWidth, centerH - buttonHeight * (i-2));
        }

        float bottomBarY = centerH - buttonHeight * 3;

        playButton.setPosition(centerW + buttonWidth + 10, bottomBarY);

        mainMenu.setPosition(centerW - buttonWidth - 10, bottomBarY);
        createAccount.setPosition(centerW, bottomBarY);
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

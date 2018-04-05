package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.screens.SetupScreen;

/**
 * Overlay displayed when creating new account.
 *
 * Allows user to type in an account name, but requires the account name to be
 * unique.
 */
public class AccountOverlay extends AbstractOverlay {
    private final Text usernameLabel;
    private final Text notice;
    private final Text error;

    private final Button mainMenu;
    private final Button createAccount;

    private TextField username;

    public AccountOverlay(ChessGame game, SetupScreen screen, AssetManager assetManager) {
        super("Create new account:", assetManager);

        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);

        usernameLabel = new Text(font);
        usernameLabel.setText("Username:");
        usernameLabel.setColor(0,0,0,1);

        notice = new Text(font);
        notice.setText("*Username has to be unique");
        notice.setColor(0,0,0,1);

        error = new Text(font);
        error.setText("");
        error.setColor(0,0,0,1);

        mainMenu = new Button("Close", assetManager);
        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setVisible(false);
                username.setText("");
            }
        });
        mainMenu.setSize(200, 75);

        createAccount = new Button("Create", assetManager);
        createAccount.setSize(200, 75);
        createAccount.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                String name = username.getText();
                if(name == null || name.trim().isEmpty())
                {
                    error.setText("* Name cannot be null or whitespace");
                    return;
                }

                if(name.toLowerCase().equals("player 1") || name.toLowerCase().equals("player 2"))
                {
                    error.setText("* Name " + name + " is reserved");
                    return;
                }

                AccountManager accountManager = game.getAccountManager();
                Account newAccount = new Account(name);

                if(!accountManager.addAccount(newAccount))
                {
                    error.setText("* " + name + " is already taken");
                    return;
                }
                // Account successfully created
                username.setText("");
                setVisible(false);
                screen.updateAccountLists(game);
                accountManager.save(AssetLoader.getAccountPath());
            }
        });

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.background = new SpriteDrawable(new Sprite(assetManager.get("button_template.png", Texture.class)));
        style.fontColor = Color.BLACK;
        style.background.setLeftWidth(25);
        style.background.setRightWidth(25);

        username = new TextField("", style);
        username.setSize(200, 50);

    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        stage.addActor(mainMenu);
        stage.addActor(createAccount);
        stage.addActor(username);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        mainMenu.setVisible(visible);
        createAccount.setVisible(visible);
        username.setVisible(visible);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        usernameLabel.setCenter(0, getY() + getHeight() /2.f+ 50.f);
        usernameLabel.setX(getX() + getWidth()/2.f - 150.f);
        usernameLabel.draw(batch, parentAlpha);

        notice.setCenter(0, getY() + getHeight() /2.f);
        notice.setX(getX() + getWidth()/2.f - 150.f);
        notice.draw(batch, parentAlpha);

        error.setCenter(0, getY() + getHeight() / 2.f - 30.f);
        error.setX(getX() + getWidth()/2.f - 150.f);
        error.draw(batch, parentAlpha);

        mainMenu.setPosition(getX() + getWidth() / 2.f - mainMenu.getWidth() - 50.f, getY() + 50);
        createAccount.setPosition(getX() + getWidth() / 2.f + 50.f, getY() + 50);
        username.setPosition(getX() + getWidth()/2.f, getY() + getHeight() /2.f+ username.getHeight()/2.f);
    }
}

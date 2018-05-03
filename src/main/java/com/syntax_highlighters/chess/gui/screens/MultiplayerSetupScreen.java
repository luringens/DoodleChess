package com.syntax_highlighters.chess.gui.screens;

import java.io.IOException;
import java.net.SocketTimeoutException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.NetworkChessGame;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.Text;
import com.syntax_highlighters.chess.network.*;
import com.badlogic.gdx.graphics.Color;

import static com.syntax_highlighters.chess.entities.Color.WHITE;
import static com.syntax_highlighters.chess.entities.Color.BLACK;

public class MultiplayerSetupScreen extends AbstractScreen {
    private AssetManager assetManager;
    private final Button mainMenuButton;
    private final Button connectButton;
    private final Button hostButton;
    private final TextField opponentTextField;
    private final Text opponentTextFieldLabel;

    public MultiplayerSetupScreen(LibgdxChessGame game) {
        super(game);
        this.assetManager = game.getAssetManager();

        mainMenuButton = new Button.Builder("Main menu", assetManager)
            .size(250, 75)
            .callback(() -> game.setScreen(new MainMenuScreen(game)))
            .stage(stage)
            .create();

        connectButton = new Button.Builder("Connect", assetManager)
            .size(250, 75)
            .callback(() -> {connect();})
            .stage(stage)
            .create();

        hostButton = new Button.Builder("Host", assetManager)
            .size(250, 75)
            .callback(() -> {host();})
            .stage(stage)
            .create();
        
        // opponent text field style
        // For more stuff on using text fields, check out 
        // gui/actors/AccountOverlay
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = AssetLoader.GetDefaultFont(assetManager);
        style.background = new WobbleDrawable(assetManager.get("button_template.png"), assetManager, Color.BLACK);
        style.fontColor = Color.BLACK;
        style.background.setLeftWidth(25);
        style.background.setRightWidth(25);
        style.cursor = new SpriteDrawable(new Sprite(assetManager.get("cursor.png", Texture.class)));

        // create text field for entering opponent to play against online
        opponentTextField = new TextField("", style);
        opponentTextField.setSize(200, 60);
        stage.addActor(opponentTextField);

        // label for opponent text field
        opponentTextFieldLabel = new Text(AssetLoader.GetDefaultFont(assetManager, false));
        opponentTextFieldLabel.setText("Enter opponent:");
        opponentTextFieldLabel.setColor(Color.BLACK);
        stage.addActor(opponentTextFieldLabel);

        Gdx.input.setInputProcessor(stage);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void host() {
        try {
            AbstractNetworkService host = new Host(30 * 1000);
            NetworkChessGame game = new NetworkChessGame(BLACK, host);
            NetworkGameScreen screen = new NetworkGameScreen(getGame(), game);
            getGame().setScreen(screen);
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void connect() {
        String address = opponentTextField.getText();
        try {
            AbstractNetworkService client = new Client(address);
            NetworkChessGame game = new NetworkChessGame(WHITE, client);
            NetworkGameScreen screen = new NetworkGameScreen(getGame(), game);
            getGame().setScreen(screen);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        float xCentre = width/2.f;
        float yCentre = height/2.f;

        float padding = 10;
        float buttonw = 250;
        float xButtonCentre = xCentre - buttonw / 2.f;
        
        // position "return to main menu" button next to "connect" button
        mainMenuButton.setPosition(xButtonCentre - buttonw - padding,
                height / 2.f - 400.f + 25.f);
        connectButton.setPosition(xButtonCentre, yCentre - 400.f + 25.f);
        hostButton.setPosition(xButtonCentre + padding + buttonw, yCentre - 400.f + 25.f);
        
        // position text field label above text field in the center of the
        // screen
        opponentTextFieldLabel.setPosition(xCentre - opponentTextFieldLabel.getWidth()/2.f,
                yCentre + opponentTextFieldLabel.getHeight()/2.f + 40);
        opponentTextField.setPosition(xCentre - opponentTextField.getWidth()/2.f,
                yCentre - opponentTextField.getHeight()/2.f);
    }
}

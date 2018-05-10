package com.syntax_highlighters.chess.gui.screens;

import static com.syntax_highlighters.chess.Color.BLACK;
import static com.syntax_highlighters.chess.Color.WHITE;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.syntax_highlighters.chess.game.NetworkChessGame;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.Text;
import com.syntax_highlighters.chess.network.AbstractNetworkService;
import com.syntax_highlighters.chess.network.Client;
import com.syntax_highlighters.chess.network.ConnectionStatus;
import com.syntax_highlighters.chess.network.Host;

public class MultiplayerSetupScreen extends AbstractScreen {
    private final TextField opponentTextField;

    private final Text statusLabel;
    private Timer statusUpdater = null;
    private boolean uiLock = false;

    private AbstractNetworkService service = null;
    private com.syntax_highlighters.chess.Color color = null;

    public MultiplayerSetupScreen(LibgdxChessGame game) {
        super(game);
        AssetManager assetManager = game.getAssetManager();

        new Button.Builder("Main menu", assetManager)
                .position(WORLDWIDTH / 2.0f - 250 / 2.f - 250 - 10, WORLDHEIGHT / 2.0f - 400.f + 25.f)
                .size(250, 75)
                .callback(() -> {
                    if (!uiLock) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                })
                .stage(stage)
                .create();

        new Button.Builder("Connect", assetManager)
                .position(WORLDWIDTH / 2.0f - 250 / 2.f, WORLDHEIGHT / 2.0f - 75.f - 40)
                .size(250, 75)
                .callback(this::connect)
                .stage(stage)
                .create();

        new Button.Builder("Host", assetManager)
                .position(WORLDWIDTH / 2.0f - 250 / 2.f + 10.f + 250.f, WORLDHEIGHT / 2.0f - 400.f + 25.f)
                .size(250, 75)
                .callback(this::host)
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
        opponentTextField.setPosition(WORLDWIDTH / 2.f - opponentTextField.getWidth() / 2.f,
                WORLDHEIGHT / 2.f - opponentTextField.getHeight() / 2.f);
        stage.addActor(opponentTextField);

        // label for opponent text field
        Text opponentTextFieldLabel = new Text(AssetLoader.GetDefaultFont(assetManager, false));
        opponentTextFieldLabel.setText("Enter opponent:");
        opponentTextFieldLabel.setColor(Color.BLACK);
        opponentTextFieldLabel.setCenter(WORLDWIDTH / 2.f, WORLDHEIGHT / 2.f + 40);
        stage.addActor(opponentTextFieldLabel);
        
        statusLabel = new Text(AssetLoader.GetDefaultFont(assetManager, false));
        statusLabel.setText("Ready!");
        statusLabel.setText("");
        statusLabel.setColor(Color.BLACK);
        stage.addActor(statusLabel);

        Gdx.input.setInputProcessor(stage);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void host() {
        if (!lockui("Waiting for someone to join")) return;
        class HostConstructor implements Runnable {
            public void run() {
                try {
                    AbstractNetworkService host = new Host(30 * 1000);
                    if (host.GetStatus() != ConnectionStatus.Connected) {
                        unlockui(host.GetLastFailureDescription());
                        return;
                    }
                    unlockui("Connected!");
                    color = BLACK;
                    service = host;
                } catch (SocketTimeoutException e) {
                    unlockui("Nobody joined.");
                } catch (IOException e) {
                    e.printStackTrace();
                    unlockui(e.getMessage());
                }
            }
        }
        Thread t = new Thread(new HostConstructor());
        t.start();
    }
    
    private void connect() {
        if (!lockui("Connecting")) return;
        String address = opponentTextField.getText();
        class ClientConstructor implements Runnable {
            public void run() {
                try {
                    AbstractNetworkService client = new Client(address);
                    if (client.GetStatus() != ConnectionStatus.Connected) {
                        unlockui(client.GetLastFailureDescription());
                        return;
                    }
                    unlockui("Connected!");
                    color = WHITE;
                    service = client;
                } catch (IOException e) {
                    e.printStackTrace();
                    unlockui(e.getMessage());
                }
            }
        }
        Thread t = new Thread(new ClientConstructor());
        t.start();
    }
    
    /** Returns whether or not the lock was gotten. */
    private boolean lockui(String message) {
        if (uiLock) return false;
        uiLock = true;
        setStatusLabel(message);

        class UpdateLabel extends TimerTask {
            int dots = 1;
            public void run() {
                String dotsstr = String.join("", Collections.nCopies(dots, "."));
                setStatusLabel(message + dotsstr);
                this.dots++;
                if (dots > 3) dots = 1;
            }
        }
        statusUpdater = new Timer();
        statusUpdater.schedule(new UpdateLabel(), 1000, 1000);

        return true;
    }
    
    private void unlockui(String message) {
        setStatusLabel(message);
        uiLock = false;
        statusUpdater.cancel();
    }

    private void setStatusLabel(String message) {
        statusLabel.setText(message);
        float x = stage.getWidth()/2.f - statusLabel.getWidth()/2.f;
        float y = stage.getHeight()/2.f - 130;
        statusLabel.setPosition(x, y);
    }

    private void nextScreen() {
        assert color != null && service != null;
        NetworkChessGame game = new NetworkChessGame(color, service);
        NetworkGameScreen screen = new NetworkGameScreen(getGame(), game);
        unlockui("");
        getGame().setScreen(screen);
    }

    @Override
    public void render(float delta) {
        if (color != null && service != null) {
            nextScreen();
        } else {
            super.render(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}

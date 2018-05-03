package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.Text;
import com.badlogic.gdx.graphics.Color;

public class MultiplayerSetupScreen extends AbstractScreen {

    private AssetManager assetManager;
    private final Button mainMenuButton;
    private final Button connectButton;
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
            .callback(() -> {throw new RuntimeException("Connection logic not yet added");})
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
    
    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        
        // position "return to main menu" button next to "connect" button
        mainMenuButton.setPosition(width / 2.f - mainMenuButton.getWidth(),
                height / 2.f - 400.f + 25.f);
        connectButton.setPosition(width/2.f, height/2.f - 400.f + 25.f);
        
        // position text field label above text field in the center of the
        // screen
        opponentTextFieldLabel.setPosition(width/2.f - opponentTextFieldLabel.getWidth()/2.f,
                height/2.f + opponentTextFieldLabel.getHeight()/2.f + 40);
        opponentTextField.setPosition(width/2.f - opponentTextField.getWidth()/2.f,
                height/2.f - opponentTextField.getHeight()/2.f);
    }
}

package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.screens.MainMenuScreen;

public class GameOverOverlay extends Overlay {
    private Text player1Name;
    private Text player1Wins;
    private Text player1Losses;

    private Text player2Name;
    private Text player2Wins;
    private Text player2Losses;

    private Button mainMenuButton;

    public GameOverOverlay(ChessGame game) {
        super("Game over", game.getAssetManager());
        AssetManager assetManager = game.getAssetManager();

        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);
        // TODO: fetch from account
        player1Name = new Text(font);
        player1Name.setText("Player 1 wins");
        player1Name.setColor(0,0,0,1);

        player1Wins = new Text(font);
        player1Wins.setText("Wins: x");
        player1Wins.setColor(0,0,0,1);

        player1Losses = new Text(font);
        player1Losses.setText("Losses: x");
        player1Losses.setColor(0,0,0,1);

        // TODO: fetch from account
        player2Name = new Text(font);
        player2Name.setText("Player 2 looses");
        player2Name.setColor(0,0,0,1);

        player2Wins = new Text(font);
        player2Wins.setText("Wins: x");
        player2Wins.setColor(0,0,0,1);

        player2Losses = new Text(font);
        player2Losses.setText("Losses: x");
        player2Losses.setColor(0,0,0,1);

        mainMenuButton = new Button("To main menu", assetManager);
        mainMenuButton.setSize(200, 75);
        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        stage.addActor(mainMenuButton);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        mainMenuButton.setVisible(visible);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        player1Name.setCenter(getX() + getWidth() / 3.f, getY() + getHeight()/2.f + 40.f);
        player1Name.draw(batch, parentAlpha);
        player1Wins.setCenter(getX() + getWidth() / 3.f, getY() + getHeight()/2.f);
        player1Wins.draw(batch, parentAlpha);
        player1Losses.setCenter(getX() + getWidth() / 3.f, getY() + getHeight()/2.f - 30.f);
        player1Losses.draw(batch, parentAlpha);

        player2Name.setCenter(getX() + getWidth() / 3.f * 2.f, getY() + getHeight()/2.f + 40.f);
        player2Name.draw(batch, parentAlpha);
        player2Wins.setCenter(getX() + getWidth() / 3.f * 2.f, getY() + getHeight()/2.f);
        player2Wins.draw(batch, parentAlpha);
        player2Losses.setCenter(getX() + getWidth() / 3.f * 2.f, getY() + getHeight()/2.f - 30.f);
        player2Losses.draw(batch, parentAlpha);

        mainMenuButton.setPosition(getX() + getWidth()/2.f - mainMenuButton.getWidth()/2.f,
                getY() + 50.f);
    }
}

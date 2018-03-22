package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.screens.MainMenuScreen;

public class GameOverOverlay extends AbstractOverlay {
    private boolean hidePlayer1 = false;
    private Text white;
    private Text player1Name;
    private Text player1Wins;
    private Text player1Losses;

    private boolean hidePlayer2 = false;
    private Text black;
    private Text player2Name;
    private Text player2Wins;
    private Text player2Losses;

    private Button mainMenuButton;

    private ChessGame game;

    public GameOverOverlay(ChessGame game) {
        super("Game over", game.getAssetManager());
        AssetManager assetManager = game.getAssetManager();

        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);

        white = new Text(font);
        white.setText("White pieces:");
        white.setColor(0,0,0,1);

        black = new Text(font);
        black.setText("Black pieces:");
        black.setColor(0,0,0,1);

        player1Name = new Text(font);
        player1Name.setText("");
        player1Name.setColor(0, 0, 0, 1);

        player1Wins = new Text(font);
        player1Wins.setText("");
        player1Wins.setColor(0, 0, 0, 1);

        player1Losses = new Text(font);
        player1Losses.setText("");
        player1Losses.setColor(0, 0, 0, 1);

        player2Name = new Text(font);
        player2Name.setText("");
        player2Name.setColor(0, 0, 0, 1);

        player2Wins = new Text(font);
        player2Wins.setText("");
        player2Wins.setColor(0, 0, 0, 1);

        player2Losses = new Text(font);
        player2Losses.setText("");
        player2Losses.setColor(0, 0, 0, 1);

        hidePlayer1 = true;
        hidePlayer2 = true;

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

    public void updateText(int winner, Account player1, Account player2, AiDifficulty ai1, AiDifficulty ai2)
    {
        hidePlayer1 = player1 == null && ai2 == null;
        if(player1 != null)
        {
            if(winner == -1)
                player1Name.setText(player1.getName() + " wins");
            else
                player1Name.setText(player1.getName() + " looses");
            player1Wins.setText("Wins: " + player1.getWinCount());
            player1Losses.setText("Losses: " + player1.getLossCount());
        }
        else if(ai1 != null)
        {
            if(winner == -1)
                player1Name.setText(ai1.name() + " Ai wins");
            else
                player1Name.setText(ai1.name() + " Ai looses");
            player1Wins.setText("");
            player1Losses.setText("");
        }

        hidePlayer2 = player2 == null && ai2 == null;
        if(player2 != null)
        {
            if(winner == 1)
                player2Name.setText(player2.getName() + " wins");
            else
                player2Name.setText(player2.getName() + " looses");

            player2Wins.setText("Wins: " + player2.getWinCount());
            player2Losses.setText("Losses: " + player2.getLossCount());
        }
        else if(ai2 != null)
        {
            if(winner == 1)
                player2Name.setText(ai2.name() + " Ai wins");
            else
                player2Name.setText(ai2.name() + " Ai looses");
            player2Wins.setText("");
            player2Losses.setText("");
        }
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if(stage == null) return;
        stage.addActor(mainMenuButton);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        mainMenuButton.setVisible(visible);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,1);
        super.draw(batch, parentAlpha);

        if(!hidePlayer1) {
            white.setCenter(getX() + getWidth() / 3.f, getY() + getHeight() / 2.f + 80.f);
            white.draw(batch, parentAlpha);
            player1Name.setCenter(getX() + getWidth() / 3.f, getY() + getHeight() / 2.f + 40.f);
            player1Name.draw(batch, parentAlpha);
            player1Wins.setCenter(getX() + getWidth() / 3.f, getY() + getHeight() / 2.f);
            player1Wins.draw(batch, parentAlpha);
            player1Losses.setCenter(getX() + getWidth() / 3.f, getY() + getHeight() / 2.f - 30.f);
            player1Losses.draw(batch, parentAlpha);
        }

        if(!hidePlayer2) {
            black.setCenter(getX() + getWidth() / 3.f * 2.f, getY() + getHeight() / 2.f + 80.f);
            black.draw(batch, parentAlpha);
            player2Name.setCenter(getX() + getWidth() / 3.f * 2.f, getY() + getHeight() / 2.f + 40.f);
            player2Name.draw(batch, parentAlpha);
            player2Wins.setCenter(getX() + getWidth() / 3.f * 2.f, getY() + getHeight() / 2.f);
            player2Wins.draw(batch, parentAlpha);
            player2Losses.setCenter(getX() + getWidth() / 3.f * 2.f, getY() + getHeight() / 2.f - 30.f);
            player2Losses.draw(batch, parentAlpha);
        }

        mainMenuButton.setPosition(getX() + getWidth()/2.f - mainMenuButton.getWidth()/2.f,
                getY() + 50.f);
    }
}

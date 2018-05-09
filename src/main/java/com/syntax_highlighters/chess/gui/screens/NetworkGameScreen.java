package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.syntax_highlighters.chess.NetworkChessGame;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.actors.Text;
import com.syntax_highlighters.chess.network.ConnectionStatus;

/** Game screen with additional network UI elements. */
public class NetworkGameScreen extends GameScreen {
    protected final Text netText;

    /** {@inheritDoc} */
    public NetworkGameScreen(LibgdxChessGame chessGame, NetworkChessGame game) {
        super(chessGame, game);

        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);
        netText = new Text(font);
        netText.setColor(0, 0, 0, 1);
        stage.addActor(netText);
        netText.setText(game.getNetworkState().toString());
    }
    
    /** {@inheritDoc} */
    @Override
    public void render(float delta) {
        NetworkChessGame game = (NetworkChessGame) this.game;
        if (game.getNetworkError() != null) {
            game.forceGameEnd();
            this.netText.setText(game.getNetworkError());
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        super.render(delta);
    }
    
    /** {@inheritDoc} */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        float midW = WORLDWIDTH / 2.f - netText.getWidth() / 2.f;
        netText.setCenter(midW, WORLDHEIGHT - 10.f);
    }

    /** {@inheritDoc} */
    @Override
    protected void gameOver(int winner) {
        NetworkChessGame game = (NetworkChessGame) this.game;
        game.disconnect();
        super.gameOver(winner);
    }
}

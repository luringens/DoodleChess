package com.syntax_highlighters.chess.gui.screens;

import com.syntax_highlighters.chess.AbstractGame;
import com.syntax_highlighters.chess.PlayerAttributes;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;

public class BurningChessScreen extends GameScreen {
    public BurningChessScreen(LibgdxChessGame chessGame, PlayerAttributes attrib1, PlayerAttributes attrib2, boolean randomBoard) {
        super(chessGame, "Fire Chess", attrib1, attrib2, randomBoard);
        historyList.setVisible(false);

        board.setPosition(WORLDWIDTH / 2.f - board.getWidth() / 2.f, WORLDHEIGHT / 2.f - board.getHeight() / 2.f);

        turnText.setCenter(WORLDWIDTH / 2.f, WORLDHEIGHT - 20.f);
        mute.setPosition(0,0);
        giveUp.setPosition(WORLDWIDTH - giveUp.getWidth() - 20.f, 20.f);
    }

    @Override
    protected void setTurnText() {
        super.setTurnText();

        turnText.setCenter(WORLDWIDTH / 2.f, WORLDHEIGHT - 20.f);
    }
}

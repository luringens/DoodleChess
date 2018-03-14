package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;

public class UiBoard extends Actor {

    public static final int SPACE_SIZE = 40;
    private Texture placeholder;
    private BitmapFont segoeUi;
    private Game game;

    private float LEGEND_OFFSET = 40;

    private IChessPiece selectedPiece = null;

    public UiBoard(Game game)
    {
        this.game = game;
        this.setWidth(40 * Board.BOARD_WIDTH);
        this.setHeight(40 * Board.BOARD_HEIGHT);
        Texture texture = new Texture(Gdx.files.internal("segoeui.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        placeholder = new Texture(Gdx.files.internal("placeholder.png"));
        segoeUi = new BitmapFont(Gdx.files.internal("segoeui.fnt"), new TextureRegion(texture), false);

        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                int px = (int) ((x-LEGEND_OFFSET) / SPACE_SIZE);
                int py = (int) ((y-LEGEND_OFFSET) / SPACE_SIZE);
                IChessPiece clicked = game.getPieceAtPosition(new Position(px+1, py+1));
                selectedPiece = clicked;
                System.out.println("down" + x + " : " + y);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                int px = (int) ((x-LEGEND_OFFSET) / SPACE_SIZE);
                int py = (int) ((y-LEGEND_OFFSET) / SPACE_SIZE);
                IChessPiece clicked = game.getPieceAtPosition(new Position(px+1, py+1));
                selectedPiece = clicked;
                System.out.println("up" + x + " : " + y + " : ");
            }
        });
    }



    private void renderBoard(Batch batch)
    {

        batch.setColor(0, 0, 0, 1);
        for(int x = 0; x < Board.BOARD_WIDTH; ++x)
            for(int y = 0; y < Board.BOARD_HEIGHT; ++y)
            {
                if((x + y) % 2 == 1) continue;
                batch.draw(placeholder, x * SPACE_SIZE + getX() + LEGEND_OFFSET, y * SPACE_SIZE + getY() + LEGEND_OFFSET, SPACE_SIZE, SPACE_SIZE);
            }

        batch.setColor(1, 1, 1, 1);
        for(int x = 0; x < Board.BOARD_WIDTH; ++x)
            for(int y = 0; y < Board.BOARD_HEIGHT; ++y)
            {
                if((x + y) % 2 == 0) continue;
                batch.draw(placeholder, x * SPACE_SIZE + getX() + LEGEND_OFFSET, y * SPACE_SIZE + getY() + LEGEND_OFFSET, SPACE_SIZE, SPACE_SIZE);
            }
    }

    private void renderPieces(Batch batch)
    {

        for(IChessPiece piece : game.getPieces())
        {
            Position pos = piece.getPosition();
            int rx = pos.getX()-1;
            int ry = pos.getY()-1;
            if(piece.isWhite())
            {
                batch.setColor(0, 1, 0, 1);
            }
            else
            {
                batch.setColor(1, 0, 0, 1);
            }
            if(piece == selectedPiece)
            {
                batch.setColor(1, 0.84f, 0, 1);
            }
            batch.draw(placeholder, rx * SPACE_SIZE + 5 + getX() + LEGEND_OFFSET, ry * SPACE_SIZE + 5 + getY() + LEGEND_OFFSET, 30, 30);
        }
    }

    private void renderMoves(Batch batch)
    {
        // TODO: Waiting for bug in allPossibleMoves to be resolved
        if(selectedPiece == null || true) return;

        for(Move move : selectedPiece.allPossibleMoves(game.getBoard()))
        {
            Position pos = move.getPosition();
            int rx = pos.getX()-1;
            int ry = pos.getY()-1;
            batch.setColor(1, 0.84f, 0, 1);
            batch.draw(placeholder, rx * SPACE_SIZE + getX() + LEGEND_OFFSET, ry * SPACE_SIZE + getY() + LEGEND_OFFSET, SPACE_SIZE, SPACE_SIZE);
        }
    }

    private void renderLegend(Batch batch)
    {
        for(int i = 0; i < Board.BOARD_WIDTH; ++i)
        {
            char pos = (char)('A' + i);
            BitmapFont.TextBounds bounds = segoeUi.getBounds("" + pos);
            float x = LEGEND_OFFSET + SPACE_SIZE/2.f + i * SPACE_SIZE + getX()  - bounds.width / 2.0f;
            float y = getY() + LEGEND_OFFSET/2.f  + bounds.height / 2.0f;
            segoeUi.draw(batch, "" + pos, x, y);
        }

        for(int i = 0; i < Board.BOARD_HEIGHT; ++i)
        {
            char pos = (char)('1' + i);
            BitmapFont.TextBounds bounds = segoeUi.getBounds("" + pos);
            float x = getX() + LEGEND_OFFSET/2.f - bounds.width/2.f;
            float y = LEGEND_OFFSET + SPACE_SIZE/2.f + i * SPACE_SIZE + getY() + bounds.height / 2.0f;
            segoeUi.draw(batch, "" + pos, x, y);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        renderLegend(batch);
        renderBoard(batch);
        renderMoves(batch);
        renderPieces(batch);
    }

}

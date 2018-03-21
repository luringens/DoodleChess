package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;

public class UiBoard extends Actor {

    private Texture tile;
    private Texture tile_black;
    private Texture tile_highlight;

    private BitmapFont segoeUi;
    private Game game;

    private AssetManager assetManager;

    private float LEGEND_OFFSET = 50;

    private IChessPiece selectedPiece = null;


    public UiBoard(AssetManager assetManager, Game game, Stage stage)
    {
        this.assetManager = assetManager;
        this.game = game;
        Texture texture = new Texture(Gdx.files.internal("segoeui.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        segoeUi = new BitmapFont(Gdx.files.internal("segoeui.fnt"), new TextureRegion(texture), false);
        tile = new Texture(Gdx.files.internal("tile.png"));
        tile_black = new Texture(Gdx.files.internal("tile_black.png"));
        tile_highlight = new Texture(Gdx.files.internal("tile_highlight.png"));

        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                // input not valid when ai
                if(game.nextPlayerIsAI()) return false;

                float tileWidth = getSpaceWidth();
                float tileHeight = getSpaceHeight();
                int px = (int) ((x-LEGEND_OFFSET) / tileWidth) + 1;
                int py = (int) ((y-LEGEND_OFFSET) / tileHeight) + 1;

                if(((UiBoard)event.getTarget()).moveSelected(px, py))
                {
                    selectedPiece = null;
                    return false;
                }

                IChessPiece clicked = game.getPieceAtPosition(new Position(px, py));
                selectedPiece = clicked;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                float tileWidth = getSpaceWidth();
                float tileHeight = getSpaceHeight();
                int px = (int) ((x-LEGEND_OFFSET) / tileWidth) + 1;
                int py = (int) ((y-LEGEND_OFFSET) / tileHeight) + 1;

                if(((UiBoard)event.getTarget()).moveSelected(px, py))
                {
                    selectedPiece = null;
                    return;
                }

                IChessPiece clicked = game.getPieceAtPosition(new Position(px, py));
                selectedPiece = clicked;
            }
        });
    }

    public float getSpaceWidth()
    {
        return (getWidth() - LEGEND_OFFSET) / Board.BOARD_WIDTH;
    }

    public float getSpaceHeight()
    {
        return (getHeight() - LEGEND_OFFSET) / Board.BOARD_HEIGHT;
    }

    private boolean moveSelected(int px, int py) {
        if(selectedPiece != null)
        {
            for(Move move : selectedPiece.allPossibleMoves(game.getBoard()))
            {
                Position pos = move.getPosition();
                if(pos.getX() == px && pos.getY() == py)
                {
                    game.performMove(selectedPiece.getPosition(), pos);
                    game.PerformAIMove();
                    return true;
                }
            }
        }
        return false;
    }


    private void renderBoard(Batch batch)
    {
        batch.setColor(1, 1, 1, 1);

        float tileWidth = getSpaceWidth();
        float tileHeight = getSpaceHeight();

        for(int x = 0; x < Board.BOARD_WIDTH; ++x)
            for(int y = 0; y < Board.BOARD_HEIGHT; ++y)
            {
                batch.draw(tile, x * tileWidth + getX() + LEGEND_OFFSET, y * tileHeight + getY() + LEGEND_OFFSET, tileWidth, tileHeight);
            }

        batch.setColor(0.4f, 0.4f, 0.4f, 1.f);
        for(int x = 0; x < Board.BOARD_WIDTH; ++x)
            for(int y = 0; y < Board.BOARD_HEIGHT; ++y)
            {
                if((x + y) % 2 == 0) continue;
                batch.draw(tile_black, x * tileWidth + getX() + LEGEND_OFFSET, y * tileHeight + getY() + LEGEND_OFFSET, tileWidth, tileHeight);
            }
    }

    private void renderPieces(Batch batch)
    {

        float tileWidth = getSpaceWidth();
        float tileHeight = getSpaceHeight();
        for(IChessPiece piece : game.getPieces()) {
            Position pos = piece.getPosition();
            int rx = pos.getX() - 1;
            int ry = pos.getY() - 1;

            if (piece == selectedPiece) {
                batch.setColor(1, 0.84f, 0, 1);
            }
            else
            {
                batch.setColor(1, 1, 1, 1);
            }

            String assetName = piece.getAssetName();
            Texture tex = assetManager.get(assetName, Texture.class);
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            float x = rx * tileWidth + 5 + getX() + LEGEND_OFFSET;
            float y = ry * tileHeight + 5 + getY() + LEGEND_OFFSET;
            float aspect = tex.getWidth() / (float) tex.getHeight();

            float height = tileHeight;
            float width = tileWidth * aspect;

            batch.draw(tex, x, y, width, height);
        }
    }

    private void renderMoves(Batch batch)
    {
        if(selectedPiece == null) return;

        float tileWidth = getSpaceWidth();
        float tileHeight = getSpaceHeight();
        for(Move move : selectedPiece.allPossibleMoves(game.getBoard()))
        {
            Position pos = move.getPosition();
            int rx = pos.getX()-1;
            int ry = pos.getY()-1;
            batch.setColor(1, 0.84f, 0, 1);
            batch.draw(tile_black, rx * tileWidth + getX() + LEGEND_OFFSET, ry * tileHeight + getY() + LEGEND_OFFSET, tileWidth, tileHeight);
        }
    }

    private void renderLegend(Batch batch)
    {
        float tileWidth = getSpaceWidth();
        float tileHeight = getSpaceHeight();
        GlyphLayout layout = new GlyphLayout();
        segoeUi.setColor(0,0,0,1);
        for(int i = 0; i < Board.BOARD_WIDTH; ++i)
        {
            char pos = (char)('A' + i);
            layout.setText(segoeUi, "" + pos);
            float x = LEGEND_OFFSET + tileWidth/2.f + i * tileWidth + getX()  - layout.width / 2.0f;
            float y = getY() + LEGEND_OFFSET/2.f  + layout.height / 2.0f;
            segoeUi.draw(batch, "" + pos, x, y);
        }

        for(int i = 0; i < Board.BOARD_HEIGHT; ++i)
        {
            char pos = (char)('1' + i);
            layout.setText(segoeUi, "" + pos);
            float x = getX() + LEGEND_OFFSET/2.f - layout.width/2.f;
            float y = LEGEND_OFFSET + tileHeight/2.f + i * tileHeight + getY() + layout.height / 2.0f;
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

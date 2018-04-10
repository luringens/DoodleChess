package com.syntax_highlighters.chess.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Game;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;

import java.util.function.Predicate;

/**
 * UI board.
 *
 * Here be dragons.
 */
public class UiBoard extends Actor {

    private final Texture tile;
    private final Texture tile_black;
    private Texture tile_highlight;

    private final BitmapFont segoeUi;
    private final Game game;

    private final AssetManager assetManager;

    private final float LEGEND_OFFSET = 50;

    private IChessPiece selectedPiece = null;

    private ShaderProgram setColorShader;
    private Color currentColor = null;

    private Color blackColor = null;
    private Color whiteColor = null;

    private Color selectedColor = new Color(1,0.84f,0, 1.0f);
    private Color threatensColor= new Color(1,0,0, 1.0f);


    public UiBoard(AssetManager assetManager, Game game, Stage stage, Color whiteColor, Color blackColor)
    {
        this.assetManager = assetManager;
        this.game = game;

        this.blackColor = blackColor;
        this.whiteColor = whiteColor;

        segoeUi = AssetLoader.GetDefaultFont(assetManager);
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

                selectedPiece = game.getPieceAtPosition(new Position(px, py));;
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

                selectedPiece = game.getPieceAtPosition(new Position(px, py));;
            }
        });

        setColorShader = new ShaderProgram(Gdx.files.internal("shaders/id.vert"), Gdx.files.internal("shaders/setColor.frag"));
        if(!setColorShader.isCompiled())
        {
            System.out.println(setColorShader.getLog());
        }
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
                if((x + y) % 2 != 0) continue;
                batch.draw(tile_black, x * tileWidth + getX() + LEGEND_OFFSET, y * tileHeight + getY() + LEGEND_OFFSET, tileWidth, tileHeight);
            }
    }

    private void renderPiece(Batch batch, IChessPiece piece, IChessPiece king, Color tint, float tileWidth, float tileHeight)
    {

        Position pos = piece.getPosition();
        int rx = pos.getX() - 1;
        int ry = pos.getY() - 1;

        if (piece == selectedPiece) {
            if(!selectedColor.equals(currentColor))
            {
                batch.end();
                batch.begin();
                setColorShader.setUniformf("u_tint", selectedColor);
                currentColor = selectedColor;
            }
        }
        else if (piece.getColor() != king.getColor() && piece.threatens(king.getPosition(), game.getBoard())) {
            if(!threatensColor.equals(currentColor))
            {
                batch.end();
                batch.begin();
                setColorShader.setUniformf("u_tint", threatensColor);
                currentColor = threatensColor;
            }
        }
        else
        {
            if(!tint.equals(currentColor))
            {
                batch.end();
                batch.begin();
                setColorShader.setUniformf("u_tint", tint);
                currentColor = tint;
            }
        }

        String assetName = piece.getAssetName();
        Texture tex = assetManager.get(assetName, Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        float x = rx * tileWidth + 5 + getX() + LEGEND_OFFSET;
        float y = ry * tileHeight + 5 + getY() + LEGEND_OFFSET;
        float aspect = tex.getWidth() / (float) tex.getHeight();

        float width = tileWidth * aspect;

        batch.draw(tex, x, y, width, tileHeight);
    }

    private void renderPieces(Batch batch)
    {
        // get the king of the current player (if the king is threatened, the
        // piece threatening it should be highlighted)
        IChessPiece king = game.getBoard().getKing(game.nextPlayerColor());

        float tileWidth = getSpaceWidth();
        float tileHeight = getSpaceHeight();

        batch.end();

        batch.setShader(setColorShader);
        batch.begin();
        final Color col = this.whiteColor;
        game.getPieces().stream().filter(x -> x.getColor().isWhite()).forEach(x -> renderPiece(batch, x, king, col, tileWidth, tileHeight));
        batch.end();

        batch.begin();
        final Color col2 = this.blackColor;
        game.getPieces().stream().filter(x -> x.getColor().isBlack()).forEach(x -> renderPiece(batch, x, king, col2, tileWidth, tileHeight));
        batch.end();


        batch.begin();
        batch.setShader(null);
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
        batch.setColor(1,1,1,1);
    }

}

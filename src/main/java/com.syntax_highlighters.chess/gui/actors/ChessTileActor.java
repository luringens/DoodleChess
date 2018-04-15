package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.List;

/**
 * Actor representing one chess tile
 */
public class ChessTileActor extends Actor {
    private boolean isBlack;
    private Position position;
    private AssetManager assetManager;
    private BoardGroup boardGroup;

    /**
     * Constructor
     * @param isBlack Whether the tile is black or not
     * @param position The position of the tile
     * @param assetManager The asset manager
     * @param boardGroup The board group that the tile is associated with
     */
    public ChessTileActor(boolean isBlack, Position position, AssetManager assetManager, BoardGroup boardGroup) {
        this.isBlack = isBlack;
        this.position = position;
        this.assetManager = assetManager;
        this.boardGroup = boardGroup;
    }

    /**
     * Internal updates
     * @param delta time since last update
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        float tileWidth = getParent().getWidth() / Board.BOARD_WIDTH;
        float tileHeight = getParent().getWidth() / Board.BOARD_HEIGHT;
        setSize(tileWidth, tileHeight);
        setPosition((position.getX()-1) * tileWidth, (position.getY()-1) * tileHeight);
    }

    /**
     * Internal drawing
     * @param batch Spritebatch
     * @param parentAlpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(1,1,1,1);
        batch.draw(assetManager.get("tile.png", Texture.class), getX(), getY(), getWidth(), getHeight());

        // If this piece is a potential target for the selected piece, draw with a golden tint.
        ChessPieceActor selected = boardGroup.getSelected();
        if(selected != null)
        {
            List<Move> moves = selected.getPiece().allPossibleMoves(boardGroup.getGame().getBoard());
            if(moves.stream().anyMatch(x->x.getPosition().equals(position)))
            {
                batch.setColor(new Color(1,0.84f,0, 1.0f));
                batch.draw(assetManager.get("tile_black.png", Texture.class), getX(), getY(), getWidth(), getHeight());
                return;
            }
        }

        // Draw the black overlay
        if(isBlack)
        {
            batch.setColor(0.4f, 0.4f, 0.4f, 0.75f);
            batch.draw(assetManager.get("tile_black.png", Texture.class), getX(), getY(), getWidth(), getHeight());
        }
    }
}

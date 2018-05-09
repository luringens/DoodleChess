package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;

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

    public Position getTilePosition() {
        return position;
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

        IChessPiece piece = boardGroup.getGame().getPieceAtPosition(this.position);
        if(piece != null) {

            boolean threat = piece.getColor() == boardGroup.getGame().nextPlayerColor().opponentColor() &&
                    piece.threatens(boardGroup.getGame().getBoard().getKing(boardGroup.getGame().nextPlayerColor()).getPosition(), boardGroup.getGame().getBoard());

            if (threat) {
                batch.setColor(Color.FIREBRICK);
                batch.draw(assetManager.get("tile_black.png", Texture.class), getX(), getY(), getWidth(), getHeight());
                return;
            }
        }


        // If this piece is a potential target for the selected piece, draw with a golden tint.
        ChessPieceActor selected = boardGroup.getSelected();
        if(selected != null)
        {
            List<Move> moves = boardGroup.getGame().allPossibleMoves(selected.getPiece());
            if(moves.stream().anyMatch(x->x.getPosition().equals(position)))
            {
                batch.setColor(new Color(1,0.84f,0, 1.0f));
                batch.draw(assetManager.get("tile_black.png", Texture.class), getX(), getY(), getWidth(), getHeight());
                return;
            }
        }

        // Highlight the tile if it is a suggested move
        Move suggested = boardGroup.getSuggestedMove();
        if (suggested != null) {
            if (position.equals(suggested.getOldPosition())
                    || position.equals(suggested.getPosition())) {
                batch.setColor(new Color(0.4f, 1.0f, 0.2f, 1.0f));
                batch.draw(assetManager.get("tile_black.png", Texture.class), getX(), getY(), getWidth(), getHeight());
            }
        }

        // Draw the black overlay
        if(isBlack)
        {
            batch.setColor(0.4f, 0.4f, 0.4f, 0.5f);
            batch.draw(assetManager.get("tile_black.png", Texture.class), getX(), getY(), getWidth(), getHeight());
        }
    }
}

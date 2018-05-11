package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.syntax_highlighters.chess.game.AbstractGame;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;
import com.syntax_highlighters.chess.gui.Audio;

/**
 * A drawable wrapper for a IChessPiece.
 */
public class ChessPieceActor extends Actor {
    private final IChessPiece piece;
    private final AbstractGame game;
    private final AssetManager assetManager;
    private final ShaderProgram setColorShader;
    private boolean isAnimating = false;
    private boolean isSelected = false;
    private Color color;

    /**
     * Constructor
     * @param piece The piece to wrap
     * @param color The color of the piece
     * @param game The game that the piece is tied to
     * @param assetManager The assetmanager
     */
    public ChessPieceActor(IChessPiece piece, Color color, AbstractGame game, AssetManager assetManager) {
        this.piece = piece;
        this.game = game;
        this.assetManager = assetManager;
        this.color = color;
        setColorShader = assetManager.get("setColor.frag");
    }

    /**
     * Get the wrapped piece
     * @return The wrapped piece
     */
    public IChessPiece getPiece() {
        return piece;
    }

    /**
     * Set piece selected, this will make it draw with a golden color.
     * @param selected whether the piece is selected or not
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Recalculate the bounds of the piece based on the size of it's parent
     */
    private void updateBounds() {
        Texture tex = assetManager.get(piece.getAssetName());
        float aspect = tex.getWidth() / (float) tex.getHeight();
        float tileWidth = this.getParent().getWidth() / Board.BOARD_WIDTH;
        float tileHeight = this.getParent().getHeight() / Board.BOARD_HEIGHT;
        setSize(tileWidth, tileHeight);
        float width = getWidth() * aspect;
        float height = getHeight();
        Position pos = piece.getPosition();
        setPosition(pos.getX() * tileWidth - tileWidth / 2.0f - width/2.0f, pos.getY() * tileHeight - tileHeight / 2.0f - height/2.0f);
    }

    /**
     * Update internal state
     * @param delta time since last update
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if(game == null) return;
        Position myPos = piece.getPosition();
        // Expected piece is not current piece, assume dead
        if(game.getPieceAtPosition(myPos) != piece)
        {
            // Could not remove self?
            if(!this.remove())
            {
                System.out.println("Could not remove dead piece");
            }
            return;
        }
        if(!isAnimating)
        {
            updateBounds();
        }
    }

    /**
     * Draw piece
     * @param batch The spritebatch
     * @param parentAlpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.flush();

        ShaderProgram prev = batch.getShader();
        batch.setColor(1,1,1,1);

        batch.setShader(setColorShader);
        if(isSelected)
            setColorShader.setUniformf("u_tint", new Color(1,0.84f,0, 1.0f));
        else
            setColorShader.setUniformf("u_tint", this.color);

        Texture tex = assetManager.get(piece.getAssetName());
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        float aspect = tex.getWidth() / (float) tex.getHeight();
        float width = getWidth() * aspect;
        float height = getHeight();
        float scaleSizeWidth = (width * getScaleX() - width);
        float scaleSizeHeight = (height * getScaleY() - height);
        batch.draw(tex, getX() - scaleSizeWidth / 2.0f, getY() + height/6.0f - scaleSizeHeight / 2.0f,
                width + scaleSizeWidth, height + scaleSizeHeight);
        batch.flush();
        batch.setShader(prev);
    }

    /**
     * Helper method for moving the piece in an animated way.
     * @param position the new position to move to
     * @param callback callback for when the animation is finished
     */
    public void animateTo(Position position, Runnable callback) {
        Texture tex = assetManager.get(piece.getAssetName());
        float aspect = tex.getWidth() / (float) tex.getHeight();
        float width = getWidth() * aspect;
        float height = getHeight();
        float tileWidth = this.getParent().getWidth() / Board.BOARD_WIDTH;
        float tileHeight = this.getParent().getHeight() / Board.BOARD_HEIGHT;
        MoveToAction action = new MoveToAction();
        action.setPosition(position.getX() * tileWidth - tileWidth / 2.0f - width/2.0f, position.getY() * tileHeight - tileHeight / 2.0f - height/2.0f);
        action.setDuration(0.4f);
        action.setInterpolation(Interpolation.pow2);
        setSize(tileWidth, tileHeight);

        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(() -> isAnimating = false);

        RunnableAction callbackAction = new RunnableAction();
        callbackAction.setRunnable(callback);

        addAction(new SequenceAction(action, callbackAction, runnableAction));

        ScaleToAction scaleAction = new ScaleToAction();
        scaleAction.setScale(1.6f);
        scaleAction.setDuration(0.2f);
        scaleAction.setInterpolation(Interpolation.linear);

        ScaleToAction scaleAction2 = new ScaleToAction();
        scaleAction2.setScale(1.0f);
        scaleAction2.setDuration(0.2f);
        scaleAction2.setInterpolation(Interpolation.linear);

        addAction(new SequenceAction(scaleAction, scaleAction2));
        isAnimating = true;
        Audio.makeMove(assetManager);
    }
}

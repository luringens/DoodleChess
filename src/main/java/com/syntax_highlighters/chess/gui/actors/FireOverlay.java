package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.BurningChess;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;

import java.util.ArrayList;
import java.util.List;

public class FireOverlay extends Actor {

    BurningChess game;
    private List<ChessTileActor> tiles;
    private List<Circle> splashes = new ArrayList<>();
    ShapeRenderer renderer;
    Circle whiteBurn;
    Circle blackBurn;

    public FireOverlay(BurningChess game, List<ChessTileActor> tiles) {
        this.game = game;
        this.tiles = tiles;
        this.renderer = new ShapeRenderer();
        whiteBurn = new Circle(0,0,0);
        blackBurn = new Circle(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),0);
    }

    private void checkCircle(Circle c) {
        for(ChessTileActor tile : tiles) {
            Position tpos = tile.getTilePosition();
            if(game.tileUnreachable().contains(tpos)) continue;
            Vector2 pos = tile.localToStageCoordinates(new Vector2(0,0));
            float x = pos.x;
            float y = pos.y;
            float w2 = tile.getWidth();
            float h2 = tile.getHeight();
            int contained = 0;
            if(c.contains(x, y) && !inSplash(x,y)) contained++;
            if(c.contains(x+w2, y) && !inSplash(x+w2,y)) contained++;
            if(c.contains(x+w2, y+h2) && !inSplash(x+w2,y+h2)) contained++;
            if(c.contains(x, y+h2) && !inSplash(x,y+h2)) contained++;
            if(contained >= 3)
            {
                IChessPiece piece = game.killTile(tpos);
                if(piece != null){
                    splashing(piece);
                }
            }
        }
    }

    private boolean inSplash(float x, float y) {
            for(Circle k : splashes){
                if(k.contains(x,y))
                    return true;
            }
            return false;
    }

    private void splashing(IChessPiece piece){
        Vector2 pos = this.localToStageCoordinates(new Vector2(0,0));
        Position piecePos = piece.getPosition();
        pos.add((piecePos.getX()-1) * (this.getParent().getWidth() / Board.BOARD_WIDTH) + (this.getParent().getWidth() / Board.BOARD_WIDTH) / 2.0f,
                (piecePos.getY()-1) * (this.getParent().getHeight() / Board.BOARD_HEIGHT) + (this.getParent().getHeight() / Board.BOARD_HEIGHT) / 2.0f);

        Circle splashcircle = new Circle(pos.x, pos.y,200.0f);
        splashes.add(splashcircle);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        game.fireTimer(delta);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float dist = (float)Math.sqrt(w*w+h*h);
        whiteBurn.setRadius(dist * game.getWhiteTimer());
        blackBurn.setRadius(dist * game.getBlackTimer());
        blackBurn.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        List<Circle> toDelete = new ArrayList<>();
        for(Circle k : splashes){
         k.radius -= 0.30;
         if(k.radius <= 0.0)
            toDelete.add(k);
        }
        splashes.removeAll(toDelete);

        checkCircle(blackBurn);
        checkCircle(whiteBurn);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.renderer.setProjectionMatrix(batch.getProjectionMatrix());
        //this.renderer.setTransformMatrix(batch.getTransformMatrix());
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1,0,0,0.25f);
        renderer.circle(whiteBurn.x, whiteBurn.y, whiteBurn.radius);
        renderer.circle(blackBurn.x, blackBurn.y, blackBurn.radius);

        renderer.setColor(0,1,0,0.25f);
        for(Circle c : splashes)
            renderer.circle(c.x, c.y, c.radius);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();

    }
}


package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.syntax_highlighters.chess.BurningChess;
import com.syntax_highlighters.chess.Position;

import java.util.List;

public class FireOverlay extends Actor {

    BurningChess game;
    private List<ChessTileActor> tiles;
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
            if(c.contains(x, y)) contained++;
            if(c.contains(x+w2, y)) contained++;
            if(c.contains(x+w2, y+h2)) contained++;
            if(c.contains(x, y+h2)) contained++;
            if(contained >= 3)
            {
                game.killTile(tpos);
            }
        }
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
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();

    }
}


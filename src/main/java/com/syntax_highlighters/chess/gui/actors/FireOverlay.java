package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.BurningChess;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.entities.IChessPiece;
import com.syntax_highlighters.chess.gui.Audio;

import java.util.ArrayList;
import java.util.List;

public class FireOverlay extends Actor {

    BurningChess game;
    private List<ChessTileActor> tiles;
    private List<Circle> splashes = new ArrayList<>();
    ShapeRenderer renderer;
    Ellipse whiteBurn;
    Ellipse blackBurn;

    FrameBuffer noise;
    FrameBuffer fire;
    ShaderProgram fireProgram;
    ShaderProgram metaballProgram;
    AssetManager manager;

    public FireOverlay(AssetManager manager, BurningChess game, List<ChessTileActor> tiles) {
        this.game = game;
        this.tiles = tiles;
        this.renderer = new ShapeRenderer();
        //whiteBurn = new Circle(0,0,0);
        //blackBurn = new Circle(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),0);
        whiteBurn = new Ellipse(0,0,0,0);
        blackBurn = new Ellipse(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), 0, 0);

        noise = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        fire = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        ShaderProgram program = manager.get("fireNoise.frag");
        if(!program.isCompiled()) System.out.println(program.getLog());

        fireProgram = manager.get("fire.frag");
        if(!fireProgram.isCompiled()) System.out.println(fireProgram.getLog());

        metaballProgram = manager.get("metaball.frag");
        if(!metaballProgram.isCompiled()) System.out.println(metaballProgram.getLog());

        Texture pixel = manager.get("pixel.png");

        SpriteBatch batch = new SpriteBatch();
        batch.setShader(program);
        Vector2 size = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        noise.begin();
        batch.begin();
        program.setUniformf("u_resolution", size);
        batch.draw(pixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        noise.end();
        batch.dispose();
        System.out.println(program.getLog());
        this.manager = manager;
    }

    private void checkCircle(Ellipse c) {
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
            if(c.contains(x + w2/2.0f, y+h2 / 2.0f) && !inSplash(x + w2/2.0f, y+h2 / 2.0f)) contained++;
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
        float v = 0.0f;
        for(Circle k : splashes){
            Vector2 ballPos = new Vector2(x - k.x, y - k.y);
            v += k.radius * k.radius / (ballPos.x * ballPos.x + ballPos.y * ballPos.y);
        }

        return v > 1.2f;
    }

    private void splashing(IChessPiece piece){
        Vector2 pos = this.localToStageCoordinates(new Vector2(0,0));
        Position piecePos = piece.getPosition();
        pos.add((piecePos.getX()-1) * (this.getParent().getWidth() / Board.BOARD_WIDTH) + (this.getParent().getWidth() / Board.BOARD_WIDTH) / 2.0f,
                (piecePos.getY()-1) * (this.getParent().getHeight() / Board.BOARD_HEIGHT) + (this.getParent().getHeight() / Board.BOARD_HEIGHT) / 2.0f);

        Circle splashcircle = new Circle(pos.x, pos.y,0.0f);

        splashes.add(splashcircle);

        CircleAnimation anim = new CircleAnimation(splashcircle, 200.0f);
        anim.setDuration(0.3f);
        anim.setInterpolation(Interpolation.exp10Out);
        CircleAnimation anim2 = new CircleAnimation(splashcircle, 0.0f);
        anim2.setDuration(BurningChess.SPLASHTIME);
        anim2.setInterpolation(Interpolation.linear);

        RunnableAction end = new RunnableAction();
        end.setRunnable(() -> splashes.remove(splashcircle));

        this.addAction(new SequenceAction(anim, anim2, end));
        Audio.SplashingSound(manager);

    }

    @Override
    public void act(float delta) {
        if(game.isGameOver())
            return;
        super.act(delta);
        game.fireTimer(delta);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float width = w * game.getWhiteTimer();
        float height = h * game.getWhiteTimer();
        float sqrt2 = (float)Math.sqrt(2.0f);
        whiteBurn.setSize(width * 2.0f * sqrt2, height * 2.0f * sqrt2);
        whiteBurn.setPosition(-width * sqrt2, -height * sqrt2);
        whiteBurn.setPosition(0,0);

        width = w * game.getBlackTimer();
        height = h * game.getBlackTimer();
        blackBurn.setSize(width * 2.0f * sqrt2, height * 2.0f * sqrt2);
        blackBurn.setPosition(w - width * sqrt2, h - height * sqrt2);
        //whiteBurn.setRadius(dist * game.getWhiteTimer());
        //blackBurn.setRadius(dist * game.getBlackTimer());
        blackBurn.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        /*List<Circle> toDelete = new ArrayList<>();
        for(Circle k : splashes){
         k.radius -= 0.30;
         if(k.radius <= 0.0)
            toDelete.add(k);
        }
        splashes.removeAll(toDelete);*/

        checkCircle(blackBurn);
        checkCircle(whiteBurn);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Matrix4 transform = batch.getTransformMatrix();
        ShaderProgram shader= batch.getShader();
        batch.setTransformMatrix(new Matrix4());
        batch.setShader(fireProgram);
        float gxW = Gdx.graphics.getWidth();
        float gxH = Gdx.graphics.getHeight();
        float ex = (whiteBurn.x) / gxW;
        float ey = (whiteBurn.y) / gxH;
        fireProgram.setUniformf("burn", ex, ey, whiteBurn.width / 2.0f / gxW, whiteBurn.height / 2.0f/ gxH);
        ex = (blackBurn.x - blackBurn.width / 8.0f) / gxW;
        ey = (blackBurn.y - blackBurn.height / 8.0f) / gxH;
        fireProgram.setUniformf("burn2", new Color(ex, ey, blackBurn.width / 4.0f / gxW, blackBurn.height / 4.0f / gxH));

        int i = 0;
        for(Circle c : splashes) {
            float nX = c.x / Gdx.graphics.getWidth();
            float nY = c.y / Gdx.graphics.getHeight();
            float nrX = c.radius / Gdx.graphics.getWidth();
            float nrY = c.radius / Gdx.graphics.getHeight();
            fireProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
            i++;
        }
        fireProgram.setUniformi("u_safePosCount", i);

        batch.draw(noise.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);


        batch.setTransformMatrix(transform );
        batch.setShader(shader);
    }

    void DrawDebug(Batch batch) {
        this.renderer.setProjectionMatrix(batch.getProjectionMatrix());
        //this.renderer.setTransformMatrix(batch.getTransformMatrix());
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1,0,0,0.25f);
        renderer.ellipse(whiteBurn.x - whiteBurn.width / 2.0f, whiteBurn.y - whiteBurn.height / 2.0f, whiteBurn.width, whiteBurn.height);
        renderer.ellipse(blackBurn.x - blackBurn.width / 2.0f, blackBurn.y - blackBurn.height / 2.0f, blackBurn.width, blackBurn.height );

        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();

        batch.setShader(metaballProgram);
        batch.setTransformMatrix(new Matrix4());
        int i = 0;
        for(Circle c : splashes) {
            float nX = c.x / Gdx.graphics.getWidth();
            float nY = c.y / Gdx.graphics.getHeight();
            float nrX = c.radius / Gdx.graphics.getWidth();
            float nrY = c.radius / Gdx.graphics.getHeight();
            metaballProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
            i++;
        }
        metaballProgram.setUniformi("u_safePosCount", i);
        batch.draw(noise.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);

    }

    private class CircleAnimation extends TemporalAction {
        Circle target;
        float destination;
        float start = 0.0f;

        public CircleAnimation(Circle t, float dst) {
            target = t;
            destination = dst;
        }

        @Override
        protected void begin() {
            super.begin();
            start = target.radius;
        }

        @Override
        protected void update(float percent) {
            target.radius = start * (1-percent) + destination * percent;
        }
    }
}


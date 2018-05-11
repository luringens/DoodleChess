package com.syntax_highlighters.chess.gui.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.game.BurningChess;
import com.syntax_highlighters.chess.Position;
import com.syntax_highlighters.chess.chesspiece.ChessPieceBishop;
import com.syntax_highlighters.chess.chesspiece.ChessPieceKnight;
import com.syntax_highlighters.chess.chesspiece.ChessPiecePawn;
import com.syntax_highlighters.chess.chesspiece.ChessPieceQueen;
import com.syntax_highlighters.chess.chesspiece.ChessPieceRook;
import com.syntax_highlighters.chess.chesspiece.IChessPiece;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.particles.Ember;
import com.syntax_highlighters.chess.gui.particles.ParticleSystem;

class FireOverlay extends Actor {

    private final BurningChess game;
    private final List<ChessTileActor> tiles;
    private final List<Circle> blacksplashes = new ArrayList<>();
    private final List<Circle> whitesplashes = new ArrayList<>();
    private final Group blackAnimations = new Group();
    private final Group whiteAnimations = new Group();
    private final ShapeRenderer renderer;
    private final Ellipse whiteBurn;
    private final Ellipse blackBurn;
    private final ParticleSystem<Ember> whiteembers;
    private final ParticleSystem<Ember> blackembers;

    private final FrameBuffer noise;
    private final FrameBuffer fire;
    private final ShaderProgram fireProgram;
    private final ShaderProgram metaballProgram;
    private final AssetManager manager;

    public FireOverlay(AssetManager manager, BurningChess game, List<ChessTileActor> tiles) {
        this.game = game;
        this.tiles = tiles;
        this.renderer = new ShapeRenderer();
        //whiteBurn = new Circle(0,0,0);
        //blackBurn = new Circle(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),0);
        whiteBurn = new Ellipse(0,0,0,0);
        blackBurn = new Ellipse(LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT, 0, 0);

        noise = new FrameBuffer(Pixmap.Format.RGBA8888, (int)LibgdxChessGame.WORLDWIDTH, (int)LibgdxChessGame.WORLDHEIGHT, false);
        fire = new FrameBuffer(Pixmap.Format.RGBA8888, (int)LibgdxChessGame.WORLDWIDTH, (int)LibgdxChessGame.WORLDHEIGHT, false);

        ShaderProgram program = manager.get("fireNoise.frag");
        if(!program.isCompiled()) System.out.println(program.getLog());

        fireProgram = manager.get("fire.frag");
        if(!fireProgram.isCompiled()) System.out.println(fireProgram.getLog());

        metaballProgram = manager.get("metaball.frag");
        if(!metaballProgram.isCompiled()) System.out.println(metaballProgram.getLog());

        Texture pixel = manager.get("pixel.png");

        SpriteBatch batch = new SpriteBatch();
        batch.setShader(program);
        Gdx.gl.glViewport(0,0, (int)LibgdxChessGame.WORLDWIDTH, (int)LibgdxChessGame.WORLDHEIGHT);
        Vector2 size = new Vector2(LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT);
        noise.begin();
        batch.begin();
        program.setUniformf("u_resolution", size);
        batch.draw(pixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        noise.end();
        batch.dispose();
        System.out.println(program.getLog());
        this.manager = manager;

        whiteembers = new ParticleSystem<>(100000, Ember.class, manager);
        blackembers = new ParticleSystem<>(100000, Ember.class, manager);
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

    private float getV(Circle k, float x, float y) {
        Vector2 ballPos = new Vector2(x - k.x, y - k.y);
        return k.radius * k.radius / (ballPos.x * ballPos.x + ballPos.y * ballPos.y);
    }

    private float splashV(float x, float y) {
        float v = 0.0f;
        for(Circle k : whitesplashes){
            v += getV(k, x, y);
        }
        for(Circle k : blacksplashes){
            v += getV(k, x, y);
        }
        return v;
    }

    private boolean inSplash(float x, float y) {
        return splashV(x, y) > 1.2f;
    }

    private void splashing(IChessPiece piece){
        Vector2 pos = this.localToStageCoordinates(new Vector2(0,0));

        float width = (this.getParent().getWidth() / Board.BOARD_WIDTH);
        float height = (this.getParent().getHeight() / Board.BOARD_HEIGHT);
        Position piecePos = piece.getPosition();
        pos.add((piecePos.getX()-1) * (this.getParent().getWidth() / Board.BOARD_WIDTH) + (this.getParent().getWidth() / Board.BOARD_WIDTH) / 2.0f,
                (piecePos.getY()-1) * (this.getParent().getHeight() / Board.BOARD_HEIGHT) + (this.getParent().getHeight() / Board.BOARD_HEIGHT) / 2.0f);

        Circle splashcircle = new Circle(pos.x, pos.y,0.0f);

       float splashSize = 200.0f;
        float tileSize = (float)Math.sqrt(width*width + height*height);
        float timeMultiplier = 1.f;
        if(piece instanceof ChessPiecePawn) {
           splashSize = tileSize * 1.f;
       }

        if(piece instanceof ChessPieceQueen) {
            splashSize = tileSize * 3.f;
            timeMultiplier= 3.f;
        }

        if(piece instanceof ChessPieceRook) {
            splashSize = tileSize * 2.f;
            timeMultiplier = 2.f;
        }

        if(piece instanceof ChessPieceBishop) {
            splashSize = tileSize * 1.5f;
            timeMultiplier = 1.5f;
        }

        if(piece instanceof ChessPieceKnight) {
            splashSize = tileSize * 1.5f;
            timeMultiplier = 1.5f;
        }
        
        if(piece.getColor() == com.syntax_highlighters.chess.Color.WHITE)
            whitesplashes.add(splashcircle);
        else
            blacksplashes.add(splashcircle);

        CircleAnimation anim = new CircleAnimation(splashcircle, splashSize);
        anim.setDuration(0.3f);
        anim.setInterpolation(Interpolation.exp10Out);
        CircleAnimation anim2 = new CircleAnimation(splashcircle, 0.0f);
        anim2.setDuration(BurningChess.SPLASHTIME * timeMultiplier);
        anim2.setInterpolation(Interpolation.linear);

        RunnableAction end = new RunnableAction();
        end.setRunnable(() -> {
            if(piece.getColor() == com.syntax_highlighters.chess.Color.WHITE)
                whitesplashes.remove(splashcircle);
            else
                blacksplashes.remove(splashcircle);
        });

        RunnableAction add = new RunnableAction();
        add.setRunnable(() -> {
            if(piece.getColor() == com.syntax_highlighters.chess.Color.WHITE)
                whiteAnimations.addAction(new SequenceAction(anim2, end));
            else
                blackAnimations.addAction(new SequenceAction(anim2, end));
        });

        this.addAction(new SequenceAction(anim, add));

        Audio.SplashingSound(manager);

    }

    @Override
    public void act(float delta) {
        if(game.isGameOver())
            return;
        super.act(delta);
        game.fireTimer(delta);
        float w = LibgdxChessGame.WORLDWIDTH;
        float h = LibgdxChessGame.WORLDHEIGHT;
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
        blackBurn.setPosition(LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT);

        if(game.nextPlayerColor() == com.syntax_highlighters.chess.Color.WHITE)
            whiteAnimations.act(delta);
        else
            blackAnimations.act(delta);

        checkCircle(blackBurn);
        checkCircle(whiteBurn);

        if(game.nextPlayerColor() == com.syntax_highlighters.chess.Color.WHITE) {
            float r = (float) Math.sqrt(whiteBurn.width * whiteBurn.width + whiteBurn.height * whiteBurn.height);
            for (int i = 0; i < Math.ceil(r / 500.f); ++i) {
                double angle = Math.random() * Math.PI / 2.f;
                float eX = (float) ((whiteBurn.width - 40.f * game.getWhiteTimer() - 10.f) / 2.0f * Math.cos(angle));
                float eY = (float) ((whiteBurn.height - 40.f * game.getWhiteTimer() - 10.f) / 2.0f * Math.sin(angle));
                float x = eX + whiteBurn.x;
                float y = eY + whiteBurn.y;
                float v = splashV(x, y);
                if (v > 1.2f) continue;
                whiteembers.spawnParticle(eX + whiteBurn.x, eY + whiteBurn.y, (float) (Math.PI + angle), (float) (10.0f * Math.random() + 40.f));
            }

            whiteembers.act(delta);
        }
        else {
            float r = (float) Math.sqrt(blackBurn.width * blackBurn.width + blackBurn.height * blackBurn.height);
            for (int i = 0; i < Math.ceil(r / 500.f); ++i) {
                double angle = Math.random() * Math.PI / 2.f + Math.PI;
                float eX = (float) ((blackBurn.width - 40.f * game.getWhiteTimer() - 10.f) / 2.0f * Math.cos(angle));
                float eY = (float) ((blackBurn.height - 40.f * game.getWhiteTimer() - 10.f) / 2.0f * Math.sin(angle));
                float x = eX + blackBurn.x;
                float y = eY + blackBurn.y;
                float v = splashV(x, y);
                if (v > 1.2f) continue;
                blackembers.spawnParticle(eX + blackBurn.x, eY + blackBurn.y, (float) (Math.PI + angle), (float) (10.0f * Math.random() + 40.f));
            }
            blackembers.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Matrix4 transform = new Matrix4(batch.getTransformMatrix());
        ShaderProgram shader= batch.getShader();
        batch.setShader(fireProgram);
        batch.setTransformMatrix(new Matrix4());
        float gxW = LibgdxChessGame.WORLDWIDTH;
        float gxH = LibgdxChessGame.WORLDHEIGHT;
        float ex = (whiteBurn.x) / gxW;
        float ey = (whiteBurn.y) / gxH;
        fireProgram.setUniformf("burn", ex, ey, whiteBurn.width / 2.0f / gxW, whiteBurn.height / 2.0f/ gxH);
        ex = (blackBurn.x) / gxW;
        ey = (blackBurn.y) / gxH;
        fireProgram.setUniformf("burn2", new Color(ex, ey, blackBurn.width / 2.0f / gxW, blackBurn.height / 2.0f / gxH));

        int i = 0;
        for(Circle c : whitesplashes) {
            float nX = c.x / LibgdxChessGame.WORLDWIDTH;
            float nY = c.y / LibgdxChessGame.WORLDHEIGHT;
            float nrX = c.radius / LibgdxChessGame.WORLDWIDTH;
            float nrY = c.radius / LibgdxChessGame.WORLDHEIGHT;
            fireProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
            i++;
        }
        for(Circle c : blacksplashes) {
            float nX = c.x / LibgdxChessGame.WORLDWIDTH;
            float nY = c.y / LibgdxChessGame.WORLDHEIGHT;
            float nrX = c.radius / LibgdxChessGame.WORLDWIDTH;
            float nrY = c.radius / LibgdxChessGame.WORLDHEIGHT;
            fireProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
            i++;
        }
        float nX = 50.f / LibgdxChessGame.WORLDWIDTH;
        float nY = 50.f / LibgdxChessGame.WORLDHEIGHT;
        float nrX = 50 / LibgdxChessGame.WORLDWIDTH;
        float nrY = 50.f / LibgdxChessGame.WORLDHEIGHT;
        fireProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
        i++;

        nX = (LibgdxChessGame.WORLDWIDTH - 110.f) / LibgdxChessGame.WORLDWIDTH;
        nY = (20.f + 75.f / 2.f) / LibgdxChessGame.WORLDHEIGHT;
        nrX = 100.f / LibgdxChessGame.WORLDWIDTH;
        nrY = 75.f/2.f / LibgdxChessGame.WORLDHEIGHT;
        fireProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
        i++;

        fireProgram.setUniformi("u_safePosCount", i);

        batch.draw(noise.getColorBufferTexture(), 0, 0, LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT, 0, 0, noise.getWidth(), noise.getHeight(), false, true);

       // batch.setTransformMatrix(transform );
       // batch.setShader(shader);
        //DrawDebug(batch);

        batch.setShader(shader);
        whiteembers.draw(batch);
        blackembers.draw(batch);
        batch.setTransformMatrix(transform );

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
        for(Circle c : whitesplashes) {
            float nX = c.x / LibgdxChessGame.WORLDWIDTH;
            float nY = c.y / LibgdxChessGame.WORLDHEIGHT;
            float nrX = c.radius / LibgdxChessGame.WORLDWIDTH;
            float nrY = c.radius / LibgdxChessGame.WORLDHEIGHT;
            metaballProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
            i++;
        }
        for(Circle c : blacksplashes) {
            float nX = c.x / LibgdxChessGame.WORLDWIDTH;
            float nY = c.y / LibgdxChessGame.WORLDHEIGHT;
            float nrX = c.radius / LibgdxChessGame.WORLDWIDTH;
            float nrY = c.radius / LibgdxChessGame.WORLDHEIGHT;
            metaballProgram.setUniformf("u_safePos[" +i + "]", new Color(nX, nY, nrX, nrY));
            i++;
        }
        metaballProgram.setUniformi("u_safePosCount", i);
        batch.draw(noise.getColorBufferTexture(), 0, 0, LibgdxChessGame.WORLDWIDTH, LibgdxChessGame.WORLDHEIGHT,
                0, 0, (int)LibgdxChessGame.WORLDWIDTH, (int)LibgdxChessGame.WORLDHEIGHT, false, true);

    }

    private class CircleAnimation extends TemporalAction {
        final Circle target;
        final float destination;
        float start = 0.0f;

        CircleAnimation(Circle t, float dst) {
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


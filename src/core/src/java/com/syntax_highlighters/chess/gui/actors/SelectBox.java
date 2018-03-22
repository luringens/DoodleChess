package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.syntax_highlighters.chess.ChessGame;
import com.syntax_highlighters.chess.gui.AssetLoader;

import java.util.ArrayList;

public class SelectBox extends Actor {
    FrameBuffer dropDown;
    Texture box;
    ArrayList<String> items = new ArrayList<>();
    boolean showDropdown = false;
    Text text;
    Texture pixel;
    SpriteBatch batch;

    public SelectBox(ChessGame game)
    {
        dropDown = new FrameBuffer(Pixmap.Format.RGBA8888, 200, 600, false);

        box = game.getAssetManager().get("button_template.png");
        pixel = game.getAssetManager().get("pixel.png");

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                updateBuffer();
                showDropdown = !showDropdown;
            }
        });

        text = new Text(AssetLoader.GetDefaultFont(game.getAssetManager()));
        text.setColor(0,0,0,1);

        batch = new SpriteBatch();
    }

    public void setItems(ArrayList<String> strings)
    {
        if(strings == null) return;
        items = strings;
    }

    private void updateBuffer()
    {
        dropDown.begin();
        batch.begin();
        batch.end();
        dropDown.end();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(box, getX(), getY(), getWidth(), getHeight());

        if(showDropdown) {
            batch.setColor(1, 1, 1, 1);
            batch.draw(pixel, getX(), getY() + getHeight(), getWidth(), 600);
            Rectangle bounds = new Rectangle((int)getX(), (int)(getY() + getHeight()), (int)getWidth(), 600);
            Rectangle scissors = new Rectangle();
            batch.flush();
            ScissorStack.calculateScissors(this.getStage().getCamera(), batch.getTransformMatrix(), bounds, scissors);
            ScissorStack.pushScissors(scissors);
            for(int i = 0; i < items.size(); ++i)
            {
                text.setText(items.get(i));
                text.setCenter(getX() + getWidth()/2.f, getY() + getHeight() + 600 + text.getHeight() - text.getHeight() * i);
                text.draw(batch, 1.f);
            }
            batch.flush();
            ScissorStack.popScissors();
        }
    }
}

package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.IChessPiece;
import com.syntax_highlighters.chess.gui.AssetLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * An actor representing the board in the chess game
 */
public class BoardGroup extends Group {
    private AbstractGame game;
    private AssetManager assetManager;
    private ChessPieceActor selected;
    private Color blackColor;
    private Color whiteColor;
    private boolean isAnimating = false;
    private Group pieceGroup;
    private final float LEGEND_OFFSET = 50;
    private PopupSelectionMenu promotionSelection;
    private Move suggestedMove = null;
    private List<ChessTileActor> tiles = new ArrayList<>();

    /**
     * Constructor
     * @param game The game that is represented
     * @param blackColor The black color
     * @param whiteColor The white color
     * @param assetManager The assetmanager
     */
    public BoardGroup(AbstractGame game, Color blackColor, Color whiteColor, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.blackColor = blackColor;
        this.whiteColor = whiteColor;

        pieceGroup = new Group();

        // Add board tiles
        for (int x = 0; x < Board.BOARD_WIDTH; ++x) {
            for (int y = 0; y < Board.BOARD_HEIGHT; ++y) {
                boolean isBlack = (x + y) % 2 == 0;
                ChessTileActor actor = new ChessTileActor(isBlack, new Position(x + 1, y + 1), assetManager, this);
                actor.addListener(new ClickListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchUp(event, x, y, pointer, button);
                        Vector2 parentCoords = actor.localToParentCoordinates(new Vector2(x, y));
                        if (tryMove(parentCoords.x, parentCoords.y)) return;
                        trySelect(null);
                    }
                });
                pieceGroup.addActor(actor);
                tiles.add(actor);
            }
        }


        // Add chess pieces
        for (IChessPiece piece : game.getPieces()) {
            addPiece(piece);
        }
        addActor(pieceGroup);

        this.promotionSelection = new PopupSelectionMenu(
                "Select promotion:",
                assetManager.get("queen_white.png", Texture.class),
                assetManager.get("rook_white.png", Texture.class),
                assetManager.get("bishop_white.png", Texture.class),
                assetManager.get("knight_white.png", Texture.class));
        this.promotionSelection.setVisible(false);

        if(game instanceof BurningChess)
            pieceGroup.addActor(new FireOverlay(assetManager, (BurningChess)game, tiles));
    }

    /**
     * Add a piece actor that is bound to a piece in the game
     *
     * @param piece The piece to bind
     */
    private ChessPieceActor newPiece(IChessPiece piece) {
        ChessPieceActor chessPieceActor = new ChessPieceActor(piece, piece.getColor().isBlack() ? this.blackColor : this.whiteColor, game, assetManager);
        chessPieceActor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Ignore use input if a piece is moving
                if (isAnimating) return false;
                // Ignore use input if ai is next player
                if (game.nextPlayerIsAI()) return false;
                Vector2 parentCoords = chessPieceActor.localToParentCoordinates(new Vector2(x, y));
                if (tryMove(parentCoords.x, parentCoords.y)) return false;
                return trySelect(chessPieceActor);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 parentCoords = chessPieceActor.localToParentCoordinates(new Vector2(x, y));
                if (tryMove(parentCoords.x, parentCoords.y)) return;
                trySelect(chessPieceActor);
            }
        });
        return chessPieceActor;
    }

    public void addPiece(IChessPiece piece) {
        pieceGroup.addActor(newPiece(piece));
    }

    /**
     * Set the size of the board.
     * Note: this will not update the children until the next update
     * @param width the new width
     * @param height the new height
     */
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);

        pieceGroup.setSize(width - LEGEND_OFFSET * 2.0f, height - LEGEND_OFFSET * 2.0f);
        pieceGroup.setPosition(LEGEND_OFFSET, LEGEND_OFFSET);
    }

    /**
     * Set stage
     * @param stage the new stage
     */
    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (this.promotionSelection.getStage() != null) {
            this.promotionSelection.remove();
        }
        if (stage == null) return;
        stage.addActor(this.promotionSelection);
    }

    /**
     * @return The selected piece, null if not piece is selected
     */
    public ChessPieceActor getSelected() {
        return selected;
    }

    /**
     * Set the selected piece to null.
     */
    public void unselectSelected() {
        if (selected != null)
            selected.setSelected(false);
        selected = null;
    }

    /**
     * @return The game that this board is wrapped to
     */
    public AbstractGame getGame() {
        return game;
    }

    /**
     * Try to perform a move for the selected piece
     * @param x the x position in coordinates relative to the board
     * @param y the y position in coordinates relative to the board
     * @return true if the move could be done, false otherwise
     */
    private boolean tryMove(float x, float y) {
        if (selected == null) return false;
        float tileWidth = pieceGroup.getWidth() / Board.BOARD_WIDTH;
        float tileHeight = pieceGroup.getHeight() / Board.BOARD_HEIGHT;

        Position estimated = new Position((int) (x / tileWidth) + 1, (int) (y / tileHeight) + 1);

        final List<Move> moves = game.getMoves(selected.getPiece().getPosition(), estimated);
        
        if (moves.size() < 1) return false;
        else if (moves.size() > 1) {
            setTouchable(Touchable.disabled);
            promotionSelection.setVisible(true);
            promotionSelection.setX(Math.min(x, getWidth() - promotionSelection.getWidth()));
            promotionSelection.setY(y);
            promotionSelection.setListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int index = (int) (x / MenuIcon.SIZE);
                    performAnimation(moves.get(index), move -> {
                        game.performMove(move);
                        clearSuggestion();
                        trySelect(null);
                        promotionSelection.setVisible(false);
                        addPiece(move.getPiece(game.getBoard()));
                        setTouchable(Touchable.enabled);
                    });
                }
            });
        }
        else {
            performAnimation(moves.get(0), move -> {
                game.performMove(move);
                clearSuggestion();
                trySelect(null);
            });
        }
        return true;
    }

    private void performAnimation(Move move, AnimationCompletedCallback callback) {
        isAnimating = true;
        final List<Move.PositionChange> posChanges = move.getPositionChanges(game.getBoard());
        for (int i = 0; i < posChanges.size(); i++) {
            Move.PositionChange change = posChanges.get(i);
            System.out.println("Animating change "  + change);
            ChessPieceActor actor = null;
            for (Actor a : pieceGroup.getChildren()) {
                if (a instanceof ChessPieceActor) {
                    actor = (ChessPieceActor)a;
                    if (actor.getPiece() == change.pieceMoved) break;
                }
            }
            if (actor == null || actor.getPiece() != change.pieceMoved) {
                Color c = change.pieceMoved.getColor() == com.syntax_highlighters.chess.entities.Color.WHITE ? whiteColor : blackColor;
                actor = new ChessPieceActor(change.pieceMoved, c, game, assetManager);
            }
            // actor should now have the correct actor
            final int index = i;
            actor.animateTo(change.newPos, () -> {
                if (index == posChanges.size()-1) {
                    isAnimating = false;
                    try {
                        callback.callback(move);
                    } catch(Exception e) {isAnimating = false;}
                    System.out.println("Animation done");
                }
            });
        }
    }

    private static interface AnimationCompletedCallback {
        void callback(Move movePerformed);
    }

    /**
     * Tries to select a piece
     * @param chessPieceActor The piece to try select, null to unset the selection
     * @return true if the piece could be selected, false otherwise
     */
    private boolean trySelect(ChessPieceActor chessPieceActor) {
        if(selected != null) selected.setSelected(false);
        selected = null;
        if (game.isGameOver()) return false;
        if (chessPieceActor == null) return false;
        if (chessPieceActor.getPiece().getColor() != game.nextPlayerColor()) return false;

        selected = chessPieceActor;
        if(selected != null) selected.setSelected(true);
        return true;
    }

    /**
     * Internal function to draw the legend
     * @param batch Spritebatch
     */
    private void drawLegend(Batch batch) {
        float tileWidth = (getWidth() - LEGEND_OFFSET * 2.0f) / Board.BOARD_WIDTH;
        float tileHeight = (getHeight() - LEGEND_OFFSET * 2.0f) / Board.BOARD_HEIGHT;
        BitmapFont font = AssetLoader.GetDefaultFont(assetManager);
        GlyphLayout layout = new GlyphLayout();
        font.setColor(0, 0, 0, 1);
        for (int i = 0; i < Board.BOARD_WIDTH; ++i) {
            char pos = (char) ('A' + i);
            layout.setText(font, "" + pos);
            float x = LEGEND_OFFSET + tileWidth / 2.f + i * tileWidth + getX() - layout.width / 2.0f;
            float y = getY() + LEGEND_OFFSET / 2.f + layout.height / 2.0f;
            font.draw(batch, "" + pos, x, y);
        }

        for (int i = 0; i < Board.BOARD_WIDTH; ++i) {
            char pos = (char) ('A' + i);
            layout.setText(font, "" + pos);
            float x = LEGEND_OFFSET + tileWidth / 2.f + i * tileWidth + getX() - layout.width / 2.0f;
            float y = getY() - LEGEND_OFFSET / 2.f + layout.height / 2.0f + getHeight();
            font.draw(batch, "" + pos, x, y);
        }

        for (int i = 0; i < Board.BOARD_HEIGHT; ++i) {
            char pos = (char) ('1' + i);
            layout.setText(font, "" + pos);
            float x = getX() + LEGEND_OFFSET / 2.f - layout.width / 2.f;
            float y = LEGEND_OFFSET + tileHeight / 2.f + i * tileHeight + getY() + layout.height / 2.0f;
            font.draw(batch, "" + pos, x, y);
        }
        for (int i = 0; i < Board.BOARD_HEIGHT; ++i) {
            char pos = (char) ('1' + i);
            layout.setText(font, "" + pos);
            float x = getX() - LEGEND_OFFSET / 2.f - layout.width / 2.f + getWidth();
            float y = LEGEND_OFFSET + tileHeight / 2.f + i * tileHeight + getY() + layout.height / 2.0f;
            font.draw(batch, "" + pos, x, y);
        }
    }

    /**
     * Internal function for drawing
     * @param batch Spritebatch
     * @param parentAlpha Parent's alpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawLegend(batch);
    }

    public Move getSuggestedMove() {
        return suggestedMove;
    }

    public void clearSuggestion() {
        suggestedMove = null;
    }

    public void showSuggestion(Move move) {
        suggestedMove = move;
    }
}

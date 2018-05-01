package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.AiDifficulty;
import com.syntax_highlighters.chess.entities.Color;
import com.syntax_highlighters.chess.entities.IChessPiece;
import com.syntax_highlighters.chess.entities.MiniMaxAIPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BurningChess extends AbstractGame{
    private float blackTimer = 0;
    private float whiteTimer = 0;
    private static final float TENMINS = 60 * 2;

    public BurningChess(AiDifficulty whiteAi, AiDifficulty blackAi) {
        if (whiteAi != null) {
            this.whiteAI = new MiniMaxAIPlayer(Color.WHITE, whiteAi);
        }
        if (blackAi != null) {
            this.blackAI = new MiniMaxAIPlayer(Color.BLACK, blackAi);
        }

        this.board = new Board();
        this.board.setupNewGame();
    }

    public BurningChess(Board board, Color nextPlayerColor) {
        this.board = board;
        this.nextPlayerColor = nextPlayerColor;
    }

    public static BurningChess setupTestBoard(Board board, Color nextPlayerColor) {
        return new BurningChess(board, nextPlayerColor);
    }

    List<Position> unreachablePos = new ArrayList<>();

    public void fireTimer(float seconds){
        if(nextPlayerColor.isWhite()) {
            whiteTimer += seconds;
        }
        if(nextPlayerColor.isBlack()) {
            blackTimer += seconds;
        }
    }

    public float getWhiteTimer() { return whiteTimer / TENMINS; }
    public float getBlackTimer() { return blackTimer / TENMINS; }

    public List<Position> tileUnreachable(){
        return unreachablePos;
    }

    public void killTile(Position tile){
        unreachablePos.add(tile);
    }


    public void killPiece(IChessPiece piece){
        board.removePiece(piece);
    }

    public void reviveTile(Position tile){
        if(unreachablePos.contains(tile))
            unreachablePos.remove(tile);
    }

    @Override
    public List<Move> allPossibleMoves() {
        return getPieces().stream()
                .filter(p -> p.getColor() == nextPlayerColor())
                .flatMap(p -> p.allPossibleMoves(getBoard()).stream())
                .filter(m -> !unreachablePos.contains(m.getPosition()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Move> allPossibleMoves(IChessPiece piece){
        if (piece.getColor() != nextPlayerColor())
            return new ArrayList<>(); // piece cannot move
        return piece.allPossibleMoves(getBoard()).stream()
                .filter(m -> !unreachablePos.contains(m.getPosition()))
                .collect(Collectors.toList()) ;
        }
    }


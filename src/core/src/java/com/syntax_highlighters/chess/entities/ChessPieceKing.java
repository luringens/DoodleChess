package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.List;
import java.util.ArrayList;
public class ChessPieceKing extends AbstractChessPiece {
    public ChessPieceKing(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -10, -20, -20, -20, -20, -20, -20, -10,
             20,  20,   0,   0,   0,   0,  20,  20,
             20,  30,  10,   0,   0,  10,  30,  20
        };
    }

    private boolean moved = false;

    public void setPieceToMoved(){
        this.moved = true;
    }

    public boolean hasMoved(){
        return moved;
    }

    private List<Position> allEnemyMoves(Board board){
        List<Position> possibleMoves = new ArrayList<>();
        for(IChessPiece a : board.getAllPieces()) {
            if (a.isWhite() != this.isWhite()) {
                Position piecePos = a.getPosition();
                if(!possibleMoves.contains(piecePos)){
                    possibleMoves.add(piecePos);
                }
            }
        }

        return possibleMoves;
    }

    private List<ChessPieceRook> myRooks(Board board){
        List<IChessPiece> pieces = board.getAllPieces();
        List<ChessPieceRook> rooks = null;
        for(IChessPiece p : pieces){
            if(p instanceof ChessPieceRook){
                rooks.add((ChessPieceRook) p);
            }
        }
        return rooks;
    }

    @Override 
    public List<Move> allPossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        int xpos = this.getPosition().getX();
        int ypos = this.getPosition().getY();
        //all fields surrounding king.
        for(int i = xpos-1; i<xpos+2; i++){
            for(int j = ypos-1; j<ypos+2; j++){
                Position boardPos = new Position(i,j);
                if(!board.isOnBoard(boardPos))
                    continue;
                if(board.isFriendly(this,boardPos))
                    continue;
                if(allEnemyMoves(board).contains(boardPos))
                    continue;
                possibleMoves.add(new Move(this.getPosition(), boardPos,this));
            }
        }

        if(!this.hasMoved()){
            Position rookrpos = new Position(this.getPosition().getX()+3,this.getPosition().getY());
            Position rooklpos = new Position(this.getPosition().getX()-4,this.getPosition().getY());
            if(board.getAtPosition(rookrpos) != null){
                if(board.getAtPosition(rookrpos) instanceof ChessPieceRook && !((ChessPieceRook) board.getAtPosition(rookrpos)).hasMoved()){
                    possibleMoves.add(new Move(this.getPosition(), new Position(this.getPosition().getX()+2, this.getPosition().getY()), this));
                }
            }
            if(board.getAtPosition(rooklpos) != null){
                if(board.getAtPosition(rooklpos) instanceof ChessPieceRook && !((ChessPieceRook) board.getAtPosition(rooklpos)).hasMoved()){
                    possibleMoves.add(new Move(this.getPosition(), new Position(this.getPosition().getX()-2, this.getPosition().getY()), this));
                }
            }

        }

        return possibleMoves;

    }

    /**
     * Check whether the king is threatened by an enemy piece.
     *
     * @param board The board
     * @return true if a piece threatens the king, false otherwise
     */
    public boolean isThreatened(Board board) {
        return allEnemyMoves(board).contains(this.getPosition());
    }

    @Override
    public IChessPiece copy() {
        return new ChessPieceKing(this.getPosition(), this.isWhite());
    }

    @Override
    public int getPieceScore() {
        return 1000;
    }

    @Override
    public String getAssetName() {
        return isWhite() ? "king_white.png" : "king_black.png";
    }
}

package com.syntax_highlighters.chess.entities;

public class ChessPieceQueen extends AbstractChessPiece {
    public ChessPieceQueen(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }
    
    @Override
    public List<Move> allPossibleMoves(Board board) {
        throw new NotImplementedException();
    }

    @Override
    public IChessPiece copy() {
        throw new NotImplementedException();
    }
    
    @Override
    public int getScore() {
        throw new NotImplementedException();
    }
}

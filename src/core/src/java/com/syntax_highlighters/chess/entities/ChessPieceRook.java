package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class ChessPieceRook extends AbstractChessPiece {
    public ChessPieceRook(Position pos, boolean isWhite) {
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

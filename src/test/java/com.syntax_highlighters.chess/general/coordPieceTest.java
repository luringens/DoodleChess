package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests ensuring that the methods of the Position class are working correctly.
 */
class coordPieceTest {

    private final Position p = new Position(3,2);
    private final String notation = "a4";
    private final Position note = Position.fromChessNotation(notation);



    @Test
    void positionXAndYValuesAreCorrect(){
        assertEquals(p.getX(),3);
        assertEquals(p.getY(),2);

    }

    @Test
    void fromChessNotationParsesStringCorrectly(){
        assertEquals(note.getX(),1);
        assertEquals(note.getY(),4);
    }


    @Test
    void northMethodMovesPositionNorthByTheCorrectAmount(){
        assertEquals(p.north(3),new Position(3,5));
    }

    @Test
    void southMethodMovesPositionSouthByTheCorrectAmount(){
        assertEquals(p.south(3),new Position(3,-1));
    }

    @Test
    void westMethodMovesPositionWestByTheCorrectAmount(){
        assertEquals(p.west(3),new Position(0,2));
    }

    @Test
    void eastMethodMovesPositionEastByTheCorrectAmount(){
        assertEquals(p.east(3),new Position(6,2));
    }
    @Test
    void northwestMethodMovesPositionNorthwestByTheCorrectAmount(){
        assertEquals(p.northwest(3),new Position(0,5));
    }

    @Test
    void northeastMethodMovesPositionNortheastByTheCorrectAmount(){
        assertEquals(p.northeast(3),new Position(6,5));
    }

    @Test
    void southeastMethodMovesPositionSoutheastByTheCorrectAmount(){
        assertEquals(p.southeast(3),new Position(6,-1));
    }

    @Test
    void southwestMethodMovesPositionSouthwestByTheCorrectAmount(){
        assertEquals(p.southwest(3),new Position(0,-1));
    }

    @Test
    void toStringMethodResultsInCorrectCoordinateRepresentation(){
        assertEquals("(3,2)",p.toString());
    }

    @Test
    void equalPositionsAreConsideredEqual(){
    assertTrue(p.equals(new Position (3,2)));
    }

    @Test
    void hashCodeReturnsTheSameValueForEqualPositions(){
        assertEquals(p.hashCode(), new Position (3,2).hashCode());
    }

}

package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests ensuring that the methods of the Position class are working correctly.
 */
public class coordPieceTest {

    Position p = new Position(3,2);
    String notation = "a4";
    Position note = Position.fromChessNotation(notation);



    @Test
    public void positionXAndYValuesAreCorrect(){
        assertEquals(p.getX(),3);
        assertEquals(p.getY(),2);

    }

    @Test
    public void fromChessNotationParsesStringCorrectly(){
        assertEquals(note.getX(),1);
        assertEquals(note.getY(),4);
    }


    @Test
    public void northMethodMovesPositionNorthByTheCorrectAmount(){
        assertEquals(p.north(3),new Position(3,5));
    }

    @Test
    public void southMethodMovesPositionSouthByTheCorrectAmount(){
        assertEquals(p.south(3),new Position(3,-1));
    }

    @Test
    public void westMethodMovesPositionWestByTheCorrectAmount(){
        assertEquals(p.west(3),new Position(0,2));
    }

    @Test
    public void eastMethodMovesPositionEastByTheCorrectAmount(){
        assertEquals(p.east(3),new Position(6,2));
    }
    @Test
    public void northwestMethodMovesPositionNorthwestByTheCorrectAmount(){
        assertEquals(p.northwest(3),new Position(0,5));
    }

    @Test
    public void northeastMethodMovesPositionNortheastByTheCorrectAmount(){
        assertEquals(p.northeast(3),new Position(6,5));
    }

    @Test
    public void southeastMethodMovesPositionSoutheastByTheCorrectAmount(){
        assertEquals(p.southeast(3),new Position(6,-1));
    }

    @Test
    public void southwestMethodMovesPositionSouthwestByTheCorrectAmount(){
        assertEquals(p.southwest(3),new Position(0,-1));
    }

    @Test
    public void toStringMethodResultsInCorrectCoordinateRepresentation(){
        assertEquals("(3,2)",p.toString());
    }

    @Test
    public void equalPositionsAreConsideredEqual(){
    assertTrue(p.equals(new Position (3,2)));
    }

    @Test
    public void hashCodeReturnsTheSameValueForEqualPositions(){
        assertEquals(p.hashCode(), new Position (3,2).hashCode());
    }

}

package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class coordPieceTest {

    Position p = new Position(3,2);
    String notation = "a4";
    Position note = Position.fromChessNotation(notation);



    @Test
    public void positiontest(){
        assertEquals(p.getX(),3);
        assertEquals(p.getY(),2);

    }

    @Test
    public void parsertest(){
        assertEquals(note.getX(),1);
        assertEquals(note.getY(),4);
    }


    @Test
    public void northNstepTest(){
        assertEquals(p.north(3),new Position(3,5));
    }

    @Test
    public void southNstepTest(){
        assertEquals(p.south(3),new Position(3,-1));
    }

    @Test
    public void westNstepTest(){
        assertEquals(p.west(3),new Position(0,2));
    }

    @Test
    public void eastNstepTest(){
        assertEquals(p.east(3),new Position(6,2));
    }
    @Test
    public void northWestNstepTest(){
        assertEquals(p.northwest(3),new Position(0,5));
    }

    @Test
    public void northEastNstepTest(){
        assertEquals(p.northeast(3),new Position(6,5));
    }

    @Test
    public void southEastNstepTest(){
        assertEquals(p.southeast(3),new Position(6,-1));
    }

    @Test
    public void southWestNstepTest(){
        assertEquals(p.southwest(3),new Position(0,-1));
    }

    @Test
    public void toStringtest(){
        assertEquals(p.toString(),"(3,2)");
    }

    @Test
    public void equalsTest(){
    assertTrue(p.equals(new Position (3,2)));
    }

    @Test
    public void hashTest(){
        assertEquals(p.hashCode(), new Position (3,2).hashCode());
    }

}

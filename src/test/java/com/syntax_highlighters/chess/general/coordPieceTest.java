package com.syntax_highlighters.chess.general;

import com.syntax_highlighters.chess.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;


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
        assertEquals(p, new Position(3, 2));
    }

    @Test
    void hashCodeReturnsTheSameValueForEqualPositions(){
        assertEquals(p.hashCode(), new Position (3,2).hashCode());
    }

    @Test
    void stepsToPositionReturnsCorrectListMovingNorth() {
        Position start = new Position(4, 4);
        Position end = new Position(4, 8);
        List<Position> answer = Arrays.asList(
                start,
                start.north(1),  // y = 5
                start.north(2),  // y = 6
                start.north(3),  // y = 7
                start.north(4)); // y = 8
        checkStepsToPosition(start, end, answer);
    }

    @Test
    void stepsToPositionReturnsCorrectListMovingSouth() {
        Position start = new Position(4, 4);
        Position end = new Position(4, 1);
        List<Position> answer = Arrays.asList(
                start,
                start.south(1),  // y = 3
                start.south(2),  // y = 2
                start.south(3)); // y = 1
        checkStepsToPosition(start, end, answer);
    }
    
    @Test
    void stepsToPositionReturnsCorrectListMovingEast() {
        Position start = new Position(4, 4);
        Position end = new Position(8, 4);
        List<Position> answer = Arrays.asList(
                start,
                start.east(1),  // x = 5
                start.east(2),  // x = 6
                start.east(3),  // x = 7
                start.east(4)); // x = 8
        checkStepsToPosition(start, end, answer);
    }
    
    @Test
    void stepsToPositionReturnsCorrectListMovingWest() {
        Position start = new Position(4, 4);
        Position end = new Position(1, 4);
        List<Position> answer = Arrays.asList(
                start,
                start.west(1),  // x = 3
                start.west(2),  // x = 2
                start.west(3)); // x = 1
        checkStepsToPosition(start, end, answer);
    }
    
    @Test
    void stepsToPositionReturnsCorrectListMovingNorthEast() {
        Position start = new Position(4, 4);
        Position end = new Position(8, 8);
        List<Position> answer = Arrays.asList(
                start,
                start.northeast(1),  // x = 5, y = 5
                start.northeast(2),  // x = 6, y = 6
                start.northeast(3),  // x = 7, y = 7
                start.northeast(4)); // x = 8, y = 8
        checkStepsToPosition(start, end, answer);
    }
    
    @Test
    void stepsToPositionReturnsCorrectListMovingNorthWest() {
        Position start = new Position(4, 4);
        Position end = new Position(1, 7);
        List<Position> answer = Arrays.asList(
                start,
                start.northwest(1),  // x = 3, y = 5
                start.northwest(2),  // x = 2, y = 6
                start.northwest(3)); // x = 1, y = 7
        checkStepsToPosition(start, end, answer);
    }
    
    @Test
    void stepsToPositionReturnsCorrectListMovingSouthEast() {
        Position start = new Position(4, 4);
        Position end = new Position(7, 1);
        List<Position> answer = Arrays.asList(
                start,
                start.southeast(1),  // x = 5, y = 3
                start.southeast(2),  // x = 6, y = 2
                start.southeast(3)); // x = 7, y = 1
        checkStepsToPosition(start, end, answer);
    }
    
    @Test
    void stepsToPositionReturnsCorrectListMovingSouthWest() {
        Position start = new Position(4, 4);
        Position end = new Position(1, 1);
        List<Position> answer = Arrays.asList(
                start,
                start.southwest(1),  // x = 3, y = 3
                start.southwest(2),  // x = 2, y = 2
                start.southwest(3)); // x = 1, y = 1
        checkStepsToPosition(start, end, answer);
    }

    @Test
    void stepsToPositionReturnsListWithStartAndEndMoveIfPositionsNotInLine() {
        Position start = new Position(4, 4);
        Position end = new Position(5, 6); // knight move
        List<Position> answer = Arrays.asList(start, end);
        checkStepsToPosition(start, end, answer);
    }

    @Test
    void stepsToPositionReturnsStartMoveTwiceIfStartEqualsEnd() {
        Position start = new Position(4, 4);
        Position end = new Position(4, 4);
        List<Position> answer = Arrays.asList(start, end);
        checkStepsToPosition(start, end, answer);
    }

    /**
     * Helper method: Checks that the central position moving to goal will yield
     * the given result.
     */
    private void checkStepsToPosition(Position start, Position end, List<Position> answer) {
        List<Position> intermediatePositions = start.stepsToPosition(end);
        assertEquals(answer, intermediatePositions);
    }
}

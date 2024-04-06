package edu.duke.zg73.battleship;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class RectangleShipTest {
    @Test
    public void test_makeCoords(){
        Coordinate upperLeft = new Coordinate(0, 0);
        int width = 2;
        int height = 3;
        HashSet<Coordinate> expected = new HashSet<>();
        expected.add(new Coordinate(0, 0));
        expected.add(new Coordinate(0, 1));
        expected.add(new Coordinate(1, 0));
        expected.add(new Coordinate(1, 1));
        expected.add(new Coordinate(2, 0));
        expected.add(new Coordinate(2, 1));
        HashSet<Coordinate> actual = RectangleShip.makeCoords(upperLeft, width, height);
        assertEquals(expected, actual);


    }

    @Test
    public void testRectangleShipConstructor() {
        Coordinate upperLeft = new Coordinate(0, 0);
        int width = 2;
        int height = 3;
        RectangleShip ship = new RectangleShip("testship", upperLeft, width, height, 's', '*');

        HashSet<Coordinate> expectedCoords = new HashSet<>();
        expectedCoords.add(new Coordinate(0, 0));
        expectedCoords.add(new Coordinate(0, 1));
        expectedCoords.add(new Coordinate(0, 2));
        expectedCoords.add(new Coordinate(1, 0));
        expectedCoords.add(new Coordinate(1, 1));
        expectedCoords.add(new Coordinate(1, 2));




        Coordinate outsideCoord = new Coordinate(3, 3);
        assertFalse(ship.occupiesCoordinates(outsideCoord));

        assertEquals("testship", ship.getName());
        assertEquals(new Coordinate(0,0), ship.getUpperLeft());
    }

}
package edu.duke.zg73.battleship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicShipTest {

    private BasicShip<Character> createTestShip() {

        return new RectangleShip<Character>(new Coordinate(0,0), 's', '*');
    }

    @Test
    public void testIsSunk() {
        BasicShip<Character> ship = createTestShip();
        assertFalse(ship.isSunk());
        assertEquals('s',ship.getDisplayInfoAt(new Coordinate(0,0), true));



        ship.recordHitAt(new Coordinate(0, 0));
        assertTrue(ship.isSunk());

        assertTrue(ship.wasHitAt(new Coordinate(0,0)));
        assertEquals('*', ship.getDisplayInfoAt(new Coordinate(0,0), true));

        assertThrows(IllegalArgumentException.class, () -> ship.getDisplayInfoAt(new Coordinate(3,3), true));
    }

    @Test
    public void test_getCoords(){
        Coordinate upperLeft = new Coordinate(0, 0);
        int width = 2;
        int height = 3;
        RectangleShip ship = new RectangleShip("testship", upperLeft, width, height, 's', '*');

        HashSet<Coordinate> expectedCoords = new HashSet<>();
        expectedCoords.add(new Coordinate(0, 0));
        expectedCoords.add(new Coordinate(1, 0));
        expectedCoords.add(new Coordinate(2, 0));
        expectedCoords.add(new Coordinate(0, 1));
        expectedCoords.add(new Coordinate(1, 1));
        expectedCoords.add(new Coordinate(2, 1));

        assertEquals(expectedCoords, ship.getCoordinates());


    }

}
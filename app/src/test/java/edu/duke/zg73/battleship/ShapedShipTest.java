package edu.duke.zg73.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ShapedShipTest {
    @Test
    public void test_makeCoords(){
        Placement p = new Placement(new Coordinate(0, 0), 'U');
        ShapedShip<Character> ship = new ShapedShip<>(p, 's', '*');
        HashSet<Coordinate> expectedCoords = new HashSet<>();
        expectedCoords.add(new Coordinate(1, 0));
        expectedCoords.add(new Coordinate(1, 1));
        expectedCoords.add(new Coordinate(1, 2));
        expectedCoords.add(new Coordinate(0, 1));
        assertEquals(expectedCoords, ship.getCoordinates());
    }

    @Test
    public void test_makeCoords2(){
        Placement p = new Placement(new Coordinate(0, 0), 'D');
        ShapedShip<Character> ship2;
        Placement p2 = new Placement(new Coordinate(0, 0), 'L');
        ship2 = new ShapedShip<>("Battleship", p, 's', '*');
        ship2 = new ShapedShip<>("Battleship", p2, 's', '*');
        ship2 = new ShapedShip<>("Carrier", p, 's', '*');
        ship2 = new ShapedShip<>("Carrier", p2, 's', '*');


    }
}
package edu.duke.zg73.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class V2ShipFactoryTest {
    private void checkShip(Ship<Character> testShip, String expectedName,
                           char expectedLetter, Coordinate... expectedLocs){
        assertEquals(expectedName, testShip.getName());
        for (Coordinate coordinate_t : expectedLocs) {
            assertEquals(expectedLetter, testShip.getDisplayInfoAt(coordinate_t, true));
            assertEquals(true, testShip.occupiesCoordinates(coordinate_t));
        }

    }

    @Test
    public void test_createship(){
        Placement v1_2 = new Placement(new Coordinate(0, 0), 'U');
        AbstractShipFactory<Character> f = new V2ShipFactory();
        Ship<Character> bat = f.makeBattleship(v1_2);
        checkShip(bat, "Battleship", 'b', new Coordinate(0, 1), new Coordinate(1,0), new Coordinate(1, 1), new Coordinate(1, 2));

    }

    @Test
    public void test_createship_exception(){
        Placement v1 = new Placement(new Coordinate(0, 0), 'R');
        AbstractShipFactory<Character> f = new V2ShipFactory();
        assertThrows(IllegalArgumentException.class, () -> f.makeDestroyer(v1));

        Placement v2 = new Placement(new Coordinate(0, 0), 'H');
        assertThrows(IllegalArgumentException.class, () -> f.makeCarrier(v2));
    }

}
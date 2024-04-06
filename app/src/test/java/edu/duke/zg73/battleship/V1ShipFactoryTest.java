package edu.duke.zg73.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class V1ShipFactoryTest {
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
        Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
        AbstractShipFactory<Character> f = new V1ShipFactory();
        Ship<Character> dst = f.makeDestroyer(v1_2);
        checkShip(dst, "Destroyer", 'd', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2));


        Placement H1_2 = new Placement(new Coordinate(1, 2), 'H');
        Ship<Character> sub = f.makeSubmarine(H1_2);
        Ship<Character> bat = f.makeBattleship(H1_2);
        Ship<Character> Car = f.makeCarrier(H1_2);


        assertThrows(IllegalArgumentException.class, () -> new Placement(new Coordinate(1, 2), 'Z'));


    }




}
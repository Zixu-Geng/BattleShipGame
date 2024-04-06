package edu.duke.zg73.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlacementTest {

    @Test
    void test_Constructor(){
        Coordinate coord = new Coordinate(2, 3);
        Placement placement = new Placement(coord, 'V');
        assertEquals(coord, placement.getWhere());
        assertEquals('V', placement.getOrientation());

        Placement placement2 = new Placement("B3H");
        assertEquals(new Coordinate(1, 3), placement2.getWhere());
        assertEquals('H', placement2.getOrientation());
    }

    @Test
    void test_Constructor_expection(){
        assertThrows(IllegalArgumentException.class, () -> new Placement(""));
        assertThrows(IllegalArgumentException.class, () -> new Placement("B3G"));

    }

    @Test
    void test_Equals(){
        Coordinate c1 = new Coordinate(2, 3);
        Placement p1 = new Placement(c1, 'v');
        Placement p2 = new Placement(c1, 'V');
        assertEquals(p1, p2);
        assertEquals(p1, p1);
        assertFalse(p1.equals(null));


    }

    @Test
    void test_newOrientation(){
        Coordinate coord = new Coordinate(2, 3);
        Placement placement = new Placement(coord, 'U');
        assertEquals(coord, placement.getWhere());
        assertEquals('U', placement.getOrientation());

        Placement placement2 = new Placement("B3V");
        assertEquals(new Coordinate(1, 3), placement2.getWhere());
        assertEquals('V', placement2.getOrientation());
    }


    @Test
    void test_ToString() {
        Placement placement = new Placement(new Coordinate(1, 2), 'V');
        String expected = "Placement{where=(1, 2), orientation=V}";
        assertEquals(expected, placement.toString());
    }

    @Test
    void test_HashCode() {
        Placement placement1 = new Placement(new Coordinate(1, 2), 'V');
        Placement placement2 = new Placement(new Coordinate(1, 2), 'V');
        assertEquals(placement1.hashCode(), placement2.hashCode());
    }

}
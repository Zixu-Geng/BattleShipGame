package edu.duke.zg73.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InBoundsRuleCheckerTest {
    @Test
    public void test_InBoundRuleChecker() {
        V1ShipFactory f = new V1ShipFactory();
        InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<Character>(null);
        BattleShipBoard<Character> b = new BattleShipBoard<Character>(10, 20, checker, 'X');

        Placement v1_1 = new Placement(new Coordinate(2, 3), 'V');
        Ship<Character> dst1 = f.makeDestroyer(v1_1);
        assertEquals(null, checker.checkMyRule(dst1, b));

        Placement v1_2 = new Placement(new Coordinate(50, 100), 'V');
        Ship<Character> dst2 = f.makeDestroyer(v1_2);
        assertEquals("That placement is invalid: the ship goes off the bottom of the board.",checker.checkMyRule(dst2, b));

        Placement v1_3 = new Placement(new Coordinate(5, 100), 'V');
        Ship<Character> dst3 = f.makeDestroyer(v1_3);
        assertEquals("That placement is invalid: the ship goes off the right of the board.",checker.checkMyRule(dst3, b));

        Placement v1_4 = new Placement(new Coordinate(-5, 100), 'V');
        Ship<Character> dst4 = f.makeDestroyer(v1_4);
        assertEquals("That placement is invalid: the ship goes off the top of the board.",checker.checkMyRule(dst4, b));

        Placement v1_5 = new Placement(new Coordinate(5, -100), 'V');
        Ship<Character> dst5 = f.makeDestroyer(v1_5);
        assertEquals("That placement is invalid: the ship goes off the left of the board.",checker.checkMyRule(dst5, b));



    }

}
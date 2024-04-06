package edu.duke.zg73.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoCollisionRuleCheckerTest {
    @Test
    public void test_NoCollisionChecker() {
        V1ShipFactory f = new V1ShipFactory();
        NoCollisionRuleChecker<Character> checker = new NoCollisionRuleChecker<>(new InBoundsRuleChecker<>(null));
        BattleShipBoard<Character> b = new BattleShipBoard<Character>(10, 10, checker, 'X');
        Ship<Character> s1 = f.makeCarrier(new Placement("A4H"));

        assertEquals(null,checker.checkPlacement(s1, b));
        b.tryAddShip(s1);

        Ship<Character> s2 = f.makeBattleship(new Placement("A7V"));

        assertEquals("The placement is invalid: this place is used by other ship.",checker.checkPlacement(s2, b));


        Ship<Character> s3 = f.makeDestroyer(new Placement("A5H"));
        assertEquals("The placement is invalid: this place is used by other ship.",checker.checkPlacement(s3, b));


        Ship<Character> s4 = f.makeDestroyer(new Placement("Z5H"));
        assertEquals("That placement is invalid: the ship goes off the bottom of the board.",checker.checkPlacement(s4, b));

    }

}
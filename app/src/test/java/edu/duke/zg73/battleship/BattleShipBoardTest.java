package edu.duke.zg73.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
    @Test
    public void test_width_and_height() {
        Board<Character> b1 = new BattleShipBoard(10,20, 'X');
        assertEquals(10, b1.getWidth());
        assertEquals(20, b1.getHeight());
    }
    @Test
    public void test_invalid_dimensions() {
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(10, 0, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(0, 20, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(10, -5, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(-8, 20, 'X'));
    }



    private <T> void checkWhatIsAtBoard(BattleShipBoard<T> board, T[][] expected) {
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
//                System.out.print(i);
//                System.out.println(j);
                assertEquals(expected[i][j], board.whatIsAtForSelf(new Coordinate(i,j)),
                        "Mismatch at coordinate: [" + i + "," + j + "]");
            }
        }
    }

    @Test
    public void test_board_empty() {
        BattleShipBoard<Character> b = new BattleShipBoard<>(10, 20, 'X');

        Character[][] expected = new Character[10][20];
        checkWhatIsAtBoard(b, expected);
    }

    @Test
    public void test_board_ship(){
        BattleShipBoard<Character> b = new BattleShipBoard<>(10, 20, 'X');

        assertEquals(null,b.tryAddShip(new RectangleShip<Character>(new Coordinate(3,5), 's', '*')));

        Character[][] expected = new Character[10][20];
        expected[3][5] = 's';
        checkWhatIsAtBoard(b, expected);
    }


    @Test
    public void test_Ship_Array() {
        BattleShipBoard<Character> b = new BattleShipBoard<>(10, 20, 'X');
        Character[][] board = new Character[10][20];
        checkWhatIsAtBoard(b, board);

        assertEquals("invalid ship to add",b.tryAddShip(new RectangleShip<Character>("Submarine", new Coordinate(3, 5), 10, 10, 's', '*')));

        b.tryAddShip(new RectangleShip<Character>(new Coordinate(1, 1), 's', '*'));
        board[1][1] = 's';
        checkWhatIsAtBoard(b, board);

    }

    @Test
    public void test_fireAt(){
        BattleShipBoard<Character> board = new BattleShipBoard<>(10,10, 'X');
        V1ShipFactory factory = new V1ShipFactory();
        Ship<Character> ship1 = factory.makeSubmarine(new Placement("B2V"));
        BoardTextView view = new BoardTextView(board);
        board.tryAddShip(ship1);

        String expectedOutput =
                        "  0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A\n" +
                        "B  | |s| | | | | | |  B\n" +
                        "C  | |s| | | | | | |  C\n" +
                        "D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9\n";

        assertEquals(expectedOutput, view.displayMyOwnBoard());
        assertSame(ship1, board.fireAt(new Coordinate(1,2)));
        assertFalse(board.is_lost());
        assertSame(ship1, board.fireAt(new Coordinate(2,2)));
        assertSame(null, board.fireAt(new Coordinate(2,3)));
        assertTrue(ship1.isSunk());
        assertTrue(board.is_lost());

    }

    @Test
    public void test_V2Factory(){

        BattleShipBoard<Character> board = new BattleShipBoard<>(4,3, 'X');
        V2ShipFactory factory = new V2ShipFactory();
        Ship<Character> ship1 = factory.makeBattleship(new Placement("A0U"));

        BoardTextView view = new BoardTextView(board);
        board.tryAddShip(ship1);


        String expected =
                "  0|1|2|3\n" +
                "A  |b| |  A\n" +
                "B b|b|b|  B\n" +
                "C  | | |  C\n" +
                "  0|1|2|3\n";
        assertEquals(expected, view.displayMyOwnBoard());


        board = new BattleShipBoard<>(4,3, 'X');
        view = new BoardTextView(board);
        ship1 = factory.makeBattleship(new Placement("A0R"));
        expected =
                "  0|1|2|3\n" +
                        "A B| | |  A\n" +
                        "B b|b| |  B\n" +
                        "C b| | |  C\n" +
                        "  0|1|2|3\n";
        board.tryAddShip(ship1);
        assertEquals(expected, view.displayMyOwnBoard());
    }

    @Test
    public void test_enemydisplay(){
        BattleShipBoard<Character> board = new BattleShipBoard<>(4,3, 'X');
        V1ShipFactory factory = new V1ShipFactory();
        Ship<Character> ship1 = factory.makeDestroyer(new Placement("A3V"));
        BoardTextView view = new BoardTextView(board);
        board.tryAddShip(ship1);

        String expectedOutput =
                "  0|1|2|3\n" +
                "A  | | |d A\n" +
                "B  | | |d B\n" +
                "C  | | |d C\n" +
                "  0|1|2|3\n";

        assertEquals(expectedOutput, view.displayMyOwnBoard());

        board.fireAt(new Coordinate(1,3));

        String expectedEnemy =
                "  0|1|2|3\n" +
                "A  | | |  A\n" +
                "B  | | |d B\n" +
                "C  | | |  C\n" +
                "  0|1|2|3\n";
        assertEquals(expectedEnemy, view.displayEnemyBoard());
    }


    @Test
    public void test_what_is_at_enemy() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(10, 20, 'X');
        V1ShipFactory f = new V1ShipFactory();
        Ship<Character> s1 = f.makeDestroyer(new Placement("A0H"));
        b1.tryAddShip(s1);
        Coordinate c1 = new Coordinate(8, 0);
        b1.fireAt(c1);
        assertEquals('X',b1.whatIsAtForEnemy(new Coordinate(8, 0)));


    }



}
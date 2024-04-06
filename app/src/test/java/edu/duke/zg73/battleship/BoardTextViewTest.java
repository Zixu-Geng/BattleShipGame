package edu.duke.zg73.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
public class BoardTextViewTest {
    private void emptyBoardHelper(int w, int h, String expectedHeader, String expectedbody){
        Board<Character> b1 = new BattleShipBoard(w, h, 'X');
        BoardTextView view = new BoardTextView(b1);
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader + expectedbody + expectedHeader;
        assertEquals(expected, view.displayMyOwnBoard());
    }

    @Test
    public void test_display_empty(){
        emptyBoardHelper(2,2, "  0|1\n", "A  |  A\nB  |  B\n");
        emptyBoardHelper(3,3, "  0|1|2\n", "A  | |  A\nB  | |  B\nC  | |  C\n" );
        emptyBoardHelper(3,2, "  0|1|2\n", "A  | |  A\nB  | |  B\n");
    }


    @Test
    public void test_invalid_board_size() {
        Board<Character> wideBoard = new BattleShipBoard(11,20, 'X');
        Board<Character> tallBoard = new BattleShipBoard(10,27, 'X');
        assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
        assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tallBoard));

    }
    private void boardWithShipsHelper(int w, int h, String expectedOutput, Ship<Character>... ships) {
        BattleShipBoard<Character> board = new BattleShipBoard<>(w, h, 'X');
        for (Ship<Character> ship : ships) {
            board.tryAddShip(ship);
        }
        BoardTextView view = new BoardTextView(board);
        assertEquals(expectedOutput, view.displayMyOwnBoard());
    }
    @Test
    public void test_display_with_ships() {
        // Create ships

        RectangleShip<Character> ship1 = new RectangleShip<Character>(new Coordinate(0,0), 's', '*');
        RectangleShip<Character> ship2 = new RectangleShip<Character>(new Coordinate(1,1), 's', '*');

        boardWithShipsHelper(2, 2, "  0|1\nA s|  A\nB  |s B\n  0|1\n", ship1, ship2);

    }

    @Test
    public void test_displayMyboardWithEnemyNextToIt(){
        Board<Character> board1 = new BattleShipBoard<Character>(10, 20, 'X');
        Board<Character> board2 = new BattleShipBoard<Character>(10, 20, 'X');
        BoardTextView view1 = new BoardTextView(board1);
        BoardTextView view2 = new BoardTextView(board2);

        String expected =
                        "     Your ocean                           Player B's ocean\n"+
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n"+
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n"+
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n"+
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n"+
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n"+
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n"+
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n"+
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n"+
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n"+
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n"+
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n"+
                        "K  | | | | | | | | |  K                K  | | | | | | | | |  K\n"+
                        "L  | | | | | | | | |  L                L  | | | | | | | | |  L\n"+
                        "M  | | | | | | | | |  M                M  | | | | | | | | |  M\n"+
                        "N  | | | | | | | | |  N                N  | | | | | | | | |  N\n"+
                        "O  | | | | | | | | |  O                O  | | | | | | | | |  O\n"+
                        "P  | | | | | | | | |  P                P  | | | | | | | | |  P\n"+
                        "Q  | | | | | | | | |  Q                Q  | | | | | | | | |  Q\n"+
                        "R  | | | | | | | | |  R                R  | | | | | | | | |  R\n"+
                        "S  | | | | | | | | |  S                S  | | | | | | | | |  S\n"+
                        "T  | | | | | | | | |  T                T  | | | | | | | | |  T\n"+
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";


        assertEquals(expected, view1.displayMyBoardWithEnemyNextToIt(view2, "Your ocean", "Player B's ocean"));

    }



}
package edu.duke.zg73.battleship;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.time.Year;
import java.util.ArrayList;

class TextPlayerTest {

    @Test
    void test_read_placement() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        TextPlayer player = createTextPlayer("A",10, 20, "B2V\nC8H\na4V\n", bytes);

        String prompt = "Please enter a location for a ship:";
        Placement[] expected = new Placement[3];
        expected[0] = new Placement(new Coordinate(1, 2), 'V');
        expected[1] = new Placement(new Coordinate(2, 8), 'H');
        expected[2] = new Placement(new Coordinate(0, 4), 'V');

        for (int i = 0; i < expected.length; i++) {
            Placement p = player.readPlacement(prompt);
            assertEquals(p, expected[i]);
            assertEquals(prompt + "\n", bytes.toString());
            bytes.reset();
        }
    }

    @Test
    void test_do_one_placement() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer("A",10, 10, "B2V\nZ2V\n", bytes);
        V1ShipFactory shipFactory = new V1ShipFactory();
        player.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));


        String output = bytes.toString();

        String expectedOutput =
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A\n" +
                "B  | |d| | | | | | |  B\n" +
                "C  | |d| | | | | | |  C\n" +
                "D  | |d| | | | | | |  D\n" +
                "E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n";
        expectedOutput = "Player " + player.name + " where would you like to place a Destroyer?\n" + expectedOutput;
        assertEquals(expectedOutput, output);
        assertThrows(IllegalArgumentException.class, ()->player.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p)));

        player.win_message();
    }


    @Test
    void test_do_one_placement_exception() throws IOException{
        StringReader sr = new StringReader("");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bytes, true);
        Board<Character> b = new BattleShipBoard<Character>(10, 20, 'X');
        TextPlayer player = new TextPlayer("A", b, new BufferedReader(sr), ps, new V1ShipFactory());
        String prompt = "Please enter a location for a ship:";
        assertThrows(EOFException.class, () -> player.readPlacement(prompt));


    }








    @Test
    void test_doaction_M3() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "B0R\nM\nB0\nG2R\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nC0\nF\nD4\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player1.playOneTurn(player2.theBoard, player2.view);
        player2.playOneTurn(player1.theBoard, player1.view);
        String output = bytes.toString();
        String expectedOutput =
                "Player A where would you like to place a Battleship?\n"+
                        "  0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A\n" +
                        "B B| | | | | | | | |  B\n" +
                        "C b|b| | | | | | | |  C\n" +
                        "D b| | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9\n"+
                        "PlayerB's turn\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player B:\n" +
                        "F Fire at a square\n" +
                        "M Move a ship to another square (3 remaining)\n" +
                        "S Sonar scan (3 remaining)\n" +
                        "Player B, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Player B, Please input attack place: \n" +
                        "Hit Battleship!\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C b| | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n"+
                        "PlayerA's turn\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B B| | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C *|b| | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D b| | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "F Fire at a square\n" +
                        "M Move a ship to another square (3 remaining)\n" +
                        "S Sonar scan (3 remaining)\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Available ships to move: \n" +
                        "Battleship\n" +
                        "Please input the ship you want to move: \n" +
                        "Please input the new place and orientation: \n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | |B| | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | |*|b| | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | |b| | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n"+
                        "PlayerB's turn\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C b| | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player B:\n" +
                        "F Fire at a square\n" +
                        "M Move a ship to another square (3 remaining)\n" +
                        "S Sonar scan (3 remaining)\n" +
                        "Player B, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Player B, Please input attack place: \n" +
                        "hit nothing\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C b| | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | |X| | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
        assertEquals(expectedOutput, output);
    }

    @Test
    void test_doaction_M2() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "B0R\nM\nB0\nZ2U\nB0\nG2U\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));

        player1.playOneTurn(player2.theBoard, player2.view);
        String output = bytes.toString();
        String expectedOutput =
                "Player A where would you like to place a Battleship?\n"+
                        "  0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A\n" +
                        "B B| | | | | | | | |  B\n" +
                        "C b|b| | | | | | | |  C\n" +
                        "D b| | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9\n"+
                        "PlayerA's turn\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B B| | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C b|b| | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D b| | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "F Fire at a square\n" +
                        "M Move a ship to another square (3 remaining)\n" +
                        "S Sonar scan (3 remaining)\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Available ships to move: \n" +
                        "Battleship\n" +
                        "Please input the ship you want to move: \n" +
                        "Please input the new place and orientation: \n" +
                        "invalid ship to add\n" +
                        "move ship again\n" +
                        "Available ships to move: \n" +
                        "Battleship\n" +
                        "Please input the ship you want to move: \n" +
                        "Please input the new place and orientation: \n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | |b| | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | |b|b|b| | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
        assertEquals(expectedOutput, output);
    }




    @Test
    void test_doaction_M() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "B0R\nM\nA0\nB0\nG2U", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player1.playOneTurn(player2.theBoard, player2.view);
        String output = bytes.toString();
        String expectedOutput =
                "Player A where would you like to place a Battleship?\n"+
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A\n" +
                "B B| | | | | | | | |  B\n" +
                "C b|b| | | | | | | |  C\n" +
                "D b| | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n"+
                "PlayerA's turn\n" +
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B B| | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C b|b| | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D b| | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "---------------------------------------------------------------------------\n" +
                "Possible actions for Player A:\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "Player A, what would you like to do?\n" +
                "---------------------------------------------------------------------------\n" +
                "Available ships to move: \n" +
                "Battleship\n" +
                "Please input the ship you want to move: \n" +
                "No Ship in your input location\n"+
                "Available ships to move: \n" +
                "Battleship\n" +
                "Please input the ship you want to move: \n" +
                "Please input the new place and orientation: \n" +
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | |b| | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | |b|b|b| | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
        assertEquals(expectedOutput, output);
    }

    @Test
    void test_playoneturn_exception2() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A", 10, 10, "\nF\nb0\n", bytes);
        TextPlayer player2 = createTextPlayer("B", 10, 10, "", bytes);

        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.playOneTurn(player2.theBoard, player2.view);
    }

    @Test
    void test_playoneturn() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "B0R\nF\nA1\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB01\nB0\nG\nF\nJ1\nF\nB0\n", bytes);

        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player2.playOneTurn(player1.theBoard, player1.view);
        player1.playOneTurn(player2.theBoard, player2.view);
        player2.playOneTurn(player1.theBoard, player1.view);
        String output = bytes.toString();
        String expectedOutput =
                "Player A where would you like to place a Battleship?\n"+
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A\n" +
                "B B| | | | | | | | |  B\n" +
                "C b|b| | | | | | | |  C\n" +
                "D b| | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n"+
                "PlayerB's turn\n" +
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "---------------------------------------------------------------------------\n" +
                "Possible actions for Player B:\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "Player B, what would you like to do?\n" +
                "---------------------------------------------------------------------------\n" +
                "Player B, Please input attack place: \n" +
                "Invalid input format\n" +
                "Player B, Please input attack place: \n" +
                "Hit Battleship!\n"+
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B b| | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "PlayerB's turn\n" +
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B b| | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "---------------------------------------------------------------------------\n" +
                "Possible actions for Player B:\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "Player B, what would you like to do?\n" +
                "---------------------------------------------------------------------------\n" +
                "Invalid action\n"+
                "Player B, Please input attack place: \n" +
                "hit nothing\n"+
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B b| | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  |X| | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "PlayerA's turn\n" +
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B *| | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C b|b| | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D b| | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "---------------------------------------------------------------------------\n" +
                "Possible actions for Player A:\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "Player A, what would you like to do?\n" +
                "---------------------------------------------------------------------------\n" +
                "Player A, Please input attack place: \n" +
                "hit nothing\n"+
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  |X| | | | | | | |  A\n" +
                "B *| | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C b|b| | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D b| | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n"+
                "PlayerB's turn\n" +
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B b| | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  |X| | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "---------------------------------------------------------------------------\n" +
                "Possible actions for Player B:\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (3 remaining)\n" +
                "S Sonar scan (3 remaining)\n" +
                "Player B, what would you like to do?\n" +
                "---------------------------------------------------------------------------\n" +
                "Player B, Please input attack place: \n" +
                "Already Hit that Place!\n"+
                "     Your ocean                           Enemy's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B b| | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J                J  |X| | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";

        try (PrintWriter out = new PrintWriter("expectedOutput.txt")) {
            out.println(expectedOutput);
        }

        // Write actual output to a text file
        try (PrintWriter out = new PrintWriter("actualOutput.txt")) {
            out.println(output);
        }
        assertEquals(expectedOutput, output);

    }

    @Test
    void test_playoneturn_move_exception() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "M\nB0R\nM\nB0\nG2U\nF\nA0\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.playOneTurn(player2.theBoard, player2.view);
        String expectedOutput =
                        "PlayerA's turn\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "F Fire at a square\n" +
                        "M Move a ship to another square (3 remaining)\n" +
                        "S Sonar scan (3 remaining)\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "No ship to move\n";
        String output = bytes.toString();
        assertEquals(expectedOutput, output);


    }



    @Test
    void test_playoneturn_move() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "B0R\nM\nB0\nG2U\nF\nA0\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
//        player2.playOneTurn(player1.theBoard, player1.view);
        player1.playOneTurn(player2.theBoard, player2.view);
        player1.playOneTurn(player2.theBoard, player2.view);
        String output = bytes.toString();
        String expectedOutput =
                        "Player A where would you like to place a Battleship?\n"+
                        "  0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A\n" +
                        "B B| | | | | | | | |  B\n" +
                        "C b|b| | | | | | | |  C\n" +
                        "D b| | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9\n"+
                        "PlayerA's turn\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B B| | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C b|b| | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D b| | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | | | | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | | | | | | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "F Fire at a square\n" +
                        "M Move a ship to another square (3 remaining)\n" +
                        "S Sonar scan (3 remaining)\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Available ships to move: \n" +
                         "Battleship\n" +
                        "Please input the ship you want to move: \n" +
                        "Please input the new place and orientation: \n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | |b| | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | |b|b|b| | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n"+
                        "PlayerA's turn\n" +
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | |b| | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | |b|b|b| | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Possible actions for Player A:\n" +
                        "F Fire at a square\n" +
                        "M Move a ship to another square (2 remaining)\n" +
                        "S Sonar scan (3 remaining)\n" +
                        "Player A, what would you like to do?\n" +
                        "---------------------------------------------------------------------------\n" +
                        "Player A, Please input attack place: \n" +
                        "hit nothing\n"+
                        "     Your ocean                           Enemy's ocean\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                        "A  | | | | | | | | |  A                A X| | | | | | | | |  A\n" +
                        "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                        "C  | | | | | | | | |  C                C  | | | | | | | | |  C\n" +
                        "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                        "E  | | | | | | | | |  E                E  | | | | | | | | |  E\n" +
                        "F  | | | | | | | | |  F                F  | | | | | | | | |  F\n" +
                        "G  | | |b| | | | | |  G                G  | | | | | | | | |  G\n" +
                        "H  | |b|b|b| | | | |  H                H  | | | | | | | | |  H\n" +
                        "I  | | | | | | | | |  I                I  | | | | | | | | |  I\n" +
                        "J  | | | | | | | | |  J                J  | | | | | | | | |  J\n" +
                        "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
//        try (PrintWriter out = new PrintWriter("expectedOutput.txt")) {
//            out.println(expectedOutput);
//        }
//
//        // Write actual output to a text file
//        try (PrintWriter out = new PrintWriter("actualOutput.txt")) {
//            out.println(output);
//        }
        assertEquals(expectedOutput, output);
    }

    @Test
    void test_doPlacementPhase() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer("A",10, 10, "A1H\nD\nB1H\nc1H\nd1H\nE1H\nf1H\ng1H\nh1H\ni1H\nj1H\n", bytes);
        player.doPlacementPhase();

        String output = bytes.toString();
        String expectedOutput = "--------------------------------------------------------------------------------\n" +
                "Player A: you are going to place the following ships (which are all\n" +
                "rectangular). For each ship, type the coordinate of the upper left\n" +
                "side of the ship, followed by either H (for horizontal) or V (for\n" +
                "vertical).  For example M4H would place a ship horizontally starting\n" +
                "at M4 and going to the right.  You have\n" +
                "\n" +
                "2 \"Submarines\" ships that are 1x2\n" +
                "3 \"Destroyers\" that are 1x3\n" +
                "3 \"Battleships\" that are 1x4\n" +
                "2 \"Carriers\" that are 1x6\n" +
                "--------------------------------------------------------------------------------\n" +
                "Player A where would you like to place a Submarine?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Submarine?\n" +
                "length of input should be 3\n"+
                "Player A where would you like to place a Submarine?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  | | | | | | | | |  C\n" +
                "D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Destroyer?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  | | | | | | | | |  D\n" +
                "E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Destroyer?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  |d|d|d| | | | | |  D\n" +
                "E  | | | | | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Destroyer?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  |d|d|d| | | | | |  D\n" +
                "E  |d|d|d| | | | | |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Battleship?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  |d|d|d| | | | | |  D\n" +
                "E  |d|d|d| | | | | |  E\n" +
                "F  |b|b|b|b| | | | |  F\n" +
                "G  | | | | | | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Battleship?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  |d|d|d| | | | | |  D\n" +
                "E  |d|d|d| | | | | |  E\n" +
                "F  |b|b|b|b| | | | |  F\n" +
                "G  |b|b|b|b| | | | |  G\n" +
                "H  | | | | | | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Battleship?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  |d|d|d| | | | | |  D\n" +
                "E  |d|d|d| | | | | |  E\n" +
                "F  |b|b|b|b| | | | |  F\n" +
                "G  |b|b|b|b| | | | |  G\n" +
                "H  |b|b|b|b| | | | |  H\n" +
                "I  | | | | | | | | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Carrier?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  |d|d|d| | | | | |  D\n" +
                "E  |d|d|d| | | | | |  E\n" +
                "F  |b|b|b|b| | | | |  F\n" +
                "G  |b|b|b|b| | | | |  G\n" +
                "H  |b|b|b|b| | | | |  H\n" +
                "I  |c|c|c|c|c|c| | |  I\n" +
                "J  | | | | | | | | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "Player A where would you like to place a Carrier?\n" +
                "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  |s|s| | | | | | |  A\n" +
                "B  |s|s| | | | | | |  B\n" +
                "C  |d|d|d| | | | | |  C\n" +
                "D  |d|d|d| | | | | |  D\n" +
                "E  |d|d|d| | | | | |  E\n" +
                "F  |b|b|b|b| | | | |  F\n" +
                "G  |b|b|b|b| | | | |  G\n" +
                "H  |b|b|b|b| | | | |  H\n" +
                "I  |c|c|c|c|c|c| | |  I\n" +
                "J  |c|c|c|c|c|c| | |  J\n" +
                "  0|1|2|3|4|5|6|7|8|9\n";

         assertEquals(expectedOutput, output);




    }


    @Test
    void test_playoneturn_exception() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",5, 5, "B9R\nF\nA1\n", bytes);
        TextPlayer player2 = createTextPlayer("B",5, 5, "F\nB0\nF\nA0\n", bytes);

        V2ShipFactory shipFactory = new V2ShipFactory();

//        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        assertThrows(IllegalArgumentException.class, ()->player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p)));

    }



    @Test
    void test_getavaiable() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",5, 5, "A0R\n", bytes);
        TextPlayer player2 = createTextPlayer("B",5, 5, "F\nA0\nF\nB0\nF\nC0\nF\nB1\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player2.playOneTurn(player1.theBoard, player1.view);

        player2.playOneTurn(player1.theBoard, player1.view);
        player2.playOneTurn(player1.theBoard, player1.view);
        ArrayList<Ship<Character>> ships = new ArrayList<>();

        assertEquals(ships, player1.theBoard.get_avaliable());

    }

    @Test
    void test_moveBattleships() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0R\nM\nA0\nB0R\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player1.playOneTurn(player2.theBoard, player2.view);
    }

    @Test
    void test_moveSub() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0V\nM\nA0\nB0V\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        player1.playOneTurn(player2.theBoard, player2.view);
    }

    @Test
    void test_moveDestroyer() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0V\nM\nA0\nB0V\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player1.playOneTurn(player2.theBoard, player2.view);
    }


    @Test
    void test_moveCarrier() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0U\nM\nA0\nB0U\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Carrier", (p) -> shipFactory.makeCarrier(p));
        player1.playOneTurn(player2.theBoard, player2.view);
    }



    @Test
    void test_movediffernetOrientation_C() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0U\nM\nA0\nD0R\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nA0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();

        player1.doOnePlacement("Carrier", (p) -> shipFactory.makeCarrier(p));
        player2.playOneTurn(player1.theBoard, player1.view);

        player1.playOneTurn(player2.theBoard, player2.view);
        ArrayList<Ship<Character>> ships = player1.theBoard.get_myShips();
        Ship<Character> ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(3,4)));


    }

    @Test
    void test_movediffernetOrientation_S() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0V\nM\nA0\nD0H\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player1.playOneTurn(player2.theBoard, player2.view);
        ArrayList<Ship<Character>> ships = player1.theBoard.get_myShips();
        Ship<Character> ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(3,1)));
    }

    @Test
    void test_movediffernetOrientation_S2() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0H\nM\nA0\nD0V\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nA1\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player1.playOneTurn(player2.theBoard, player2.view);
        ArrayList<Ship<Character>> ships = player1.theBoard.get_myShips();
        Ship<Character> ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(4,0)));
    }

    @Test
    void test_movediffernetOrientation_B() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0U\nM\nB0\na0R\nm\na0\na0d\nm\na0\na0u\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        ArrayList<Ship<Character>> ships;
        Ship<Character> ship;

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,0)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,2)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(1,0)));

    }


    @Test
    void test_movediffernetOrientation_B2() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "a0u\nM\nB0\nA0D\nM\nA1\nA0L\nM\nA1\nA0U\nM\nB0\nA0L\nF\na0\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);

        ArrayList<Ship<Character>> ships;
        Ship<Character> ship;

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,2)));

        System.out.println("===");
        player1.playOneTurn(player2.theBoard, player1.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(2,1)));

        player1.playOneTurn(player2.theBoard, player1.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);

        player1.playOneTurn(player2.theBoard, player1.view);
        assertTrue(ship.wasHitAt(new Coordinate(1,0)));


    }

    @Test
    void test_movediffernetOrientation_B3() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0U\nm\nB0\nA0L\nm\nb0\na0d\nm\na0\na0R\nm\na0\na0u\nm\nb0\na0r\nm\na0\na0l\nm\nb0\na0r\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);

        ArrayList<Ship<Character>> ships;
        Ship<Character> ship;

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(2,1)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,2)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,0)));

        player1.skill_count.put("M Move a ship to another square", 4);
        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(1,0)));

        player1.playOneTurn(player2.theBoard, player2.view);

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(2,1)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);

        assertTrue(ship.wasHitAt(new Coordinate(0,0)));

    }

    @Test
    void test_movediffernetOrientation_C1() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10,
                "A0U\nM\nA0\na0R\nm\nb0\na0u\nm\na0\na0D\nm\na0\na0U\nm\na0\na0l\nm\nb0\na0u\nm\na0\na0r\n" +
                "m\nb0\na0d\nm\nb0\na0r\nm\nb0\na0l\nm\nb0\na0r\nm\nb0\na0d\n" +
                        "m\na0\na0l\nm\nb0\na0d\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nA0\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.skill_count.put("M Move a ship to another square", 100);

        player1.doOnePlacement("Carrier", (p) -> shipFactory.makeCarrier(p));
        player2.playOneTurn(player1.theBoard, player1.view);

        ArrayList<Ship<Character>> ships;
        Ship<Character> ship;

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,4)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,0)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(4,1)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,0)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(1,0)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,0)));

        player1.playOneTurn(player2.theBoard, player2.view);

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(4,1)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,4)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(1,0)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(0,4)));

        player1.playOneTurn(player2.theBoard, player2.view);

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(1,0)));

        player1.playOneTurn(player2.theBoard, player2.view);
        ships = player1.theBoard.get_myShips();
        ship = ships.get(0);
        assertTrue(ship.wasHitAt(new Coordinate(4,1)));








    }




    @Test
    void test_scan() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0R\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB1\nS\nB2\n", bytes);

        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player2.playOneTurn(player1.theBoard, player1.view);

    }


    @Test
    void test_scan2() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0U\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB1\nS\nB2\n", bytes);

        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Carrier", (p) -> shipFactory.makeCarrier(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player2.playOneTurn(player1.theBoard, player1.view);

    }


    @Test
    void test_scan3() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0V\nA1H\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nB1\nS\nB2\n", bytes);
        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        player1.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        player2.playOneTurn(player1.theBoard, player1.view);
        player2.playOneTurn(player1.theBoard, player1.view);


    }



    @Test
    void test_scan_exception() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0R\n", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "S\nB11\nS\nB1\n", bytes);



        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);

    }


    @Test
    void test_move_afterfire() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0H\nm\na0\nj0h", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\nj0\nF\nj0\n", bytes);



        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        player2.playOneTurn(player1.theBoard, player1.view);

        player1.playOneTurn(player2.theBoard, player2.view);
        player2.playOneTurn(player1.theBoard, player1.view);


    }

    @Test
    void test_move_afterfire2() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0H\nm\na0\nj0h", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\na0\nF\nj0\n", bytes);



        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Submarine", (p) -> shipFactory.makeSubmarine(p));
        player2.playOneTurn(player1.theBoard, player1.view);

        player1.playOneTurn(player2.theBoard, player2.view);
        player2.playOneTurn(player1.theBoard, player1.view);


    }

    @Test
    void test_move_afterfire_shaped() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player1 = createTextPlayer("A",10, 10, "A0R\nm\na0\nh0R", bytes);
        TextPlayer player2 = createTextPlayer("B",10, 10, "F\na0\nF\nh0\n", bytes);



        V2ShipFactory shipFactory = new V2ShipFactory();
        player1.doOnePlacement("Battleship", (p) -> shipFactory.makeBattleship(p));
        player2.playOneTurn(player1.theBoard, player1.view);

        player1.playOneTurn(player2.theBoard, player2.view);
        player2.playOneTurn(player1.theBoard, player1.view);


    }


    private TextPlayer createTextPlayer(String name, int w, int h, String inputData, OutputStream bytes) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V1ShipFactory shipFactory = new V1ShipFactory();
        return new TextPlayer(name, board, input, output, shipFactory);
    }

}
package edu.duke.zg73.battleship;

import java.io.Serializable;
import java.util.function.Function;


/**
 * This class handles textual display of
 * a Board (i.e., converting it to a string to show
 * to the user).
 * It supports two ways to display the Board:
 * one for the player's own board, and one for the
 * enemy's board.
 */


public class BoardTextView implements Serializable {
    /**
     * The Board to display
     */
    private Board<Character> toDisplay;

    /**
     * Constructs a BoardView, given the board it will display.
     *
     * @param toDisplay is the Board to display
     * @throws IllegalArgumentException if the board is larger than 10x26.
     */
    public BoardTextView(Board<Character> toDisplay) {
        this.toDisplay = toDisplay;
        if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
            throw new IllegalArgumentException(
                    "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
        }
    }


    public char[][] createAnyBoardArray(Function<Coordinate, Character> getSquareFn, int height, int width) {
        char[][] boardArray = new char[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Character squareContent = getSquareFn.apply(new Coordinate(row, col));
                boardArray[row][col] = (squareContent != null) ? squareContent : ' ';
            }
        }
        return boardArray;
    }

    public char[][] createMyOwnBoardArray() {
        return createAnyBoardArray((c) -> toDisplay.whatIsAtForSelf(c), toDisplay.getHeight(), toDisplay.getWidth());
    }

    public char[][] createEnemyBoardArray() {
        return createAnyBoardArray((c) -> toDisplay.whatIsAtForEnemy(c), toDisplay.getHeight(), toDisplay.getWidth());
    }



    public String displayMyOwnBoard(){

        return displayAnyBoard((c)->toDisplay.whatIsAtForSelf(c));
    }

    public String displayEnemyBoard(){
        return displayAnyBoard((c)->toDisplay.whatIsAtForEnemy(c));
    }

    /**
     * Get the Board in String
     * @return the Board to print
     */
    public String displayAnyBoard(Function<Coordinate, Character> getSquareFn){
        StringBuilder sb = new StringBuilder();
        String header = makeHeader();
        sb.append(header);

        char rowLabel = 'A';
        for (int row = 0; row < toDisplay.getHeight(); row++) {
            sb.append(rowLabel).append(" ");
            for (int col = 0; col < toDisplay.getWidth(); col++) {
                Character ship = getSquareFn.apply(new Coordinate(row, col));
                if (ship != null) {
                    sb.append(ship);

                } else {
                    sb.append(" ");
                }
                if (col < toDisplay.getWidth() - 1) {
                    sb.append("|");
                }
            }
            sb.append(" ").append(rowLabel);
            sb.append("\n");
            rowLabel++;
        }

        sb.append(header);

        return sb.toString();
    }


    /**
     * Display myboard and enemyboard together
     * @param enemyView is the enemy view
     * @param myHeader is the header of myboard
     * @param enemyHeader is the header of enemyboard
     * @return display info
     */
    public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader) {

        StringBuilder sb = new StringBuilder();

        String[] myBoard_display = displayMyOwnBoard().split("\n");
        String[] enemyBoard_display = enemyView.displayEnemyBoard().split("\n");
        sb.append("     " + myHeader + " ".repeat(2*toDisplay.getWidth() + 17 - myHeader.length()) + enemyHeader + '\n');
        sb.append(myBoard_display[0]).append(" ".repeat(2*toDisplay.getWidth() + 19 - myBoard_display[0].length()) ).append(enemyBoard_display[0]).append("\n");
        for(int i =1; i<myBoard_display.length-1; i++){
            sb.append(myBoard_display[i]).append(" ".repeat(16)).append(enemyBoard_display[i]).append("\n");
        }
        sb.append(myBoard_display[myBoard_display.length-1]).append(" ".repeat(2*toDisplay.getWidth() + 19 - myBoard_display[myBoard_display.length-1].length())).append(enemyBoard_display[myBoard_display.length-1]).append("\n");
        return sb.toString();

    }


    /**
     * Make the header in the form of 1|2|3|....
     * @return the made header
     */
    String makeHeader() {
        StringBuilder ans = new StringBuilder("  ");
        String sep="";
        for (int i = 0; i < toDisplay.getWidth(); i++) {
            ans.append(sep);
            ans.append(i);

            sep = "|";
        }
        ans.append("\n");
        return ans.toString();
    }

    public void updateBoard(Board<Character> new_Display){
        toDisplay = new_Display;

    }
}



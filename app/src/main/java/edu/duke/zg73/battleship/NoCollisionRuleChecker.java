package edu.duke.zg73.battleship;

import java.io.Serializable;

public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> implements Serializable {

    public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
        super(next);

    }


    /**
     * check the validation of put a ship in a certain board, the will check the coordinate is in the board
     * @param theShip is the ship to check
     * @param theBoard is the borad to display
     * @return the error message
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {

        for (Coordinate coordinate : theShip.getCoordinates()) {
            if (theBoard.whatIsAtForSelf(coordinate) != null) {
                return "The placement is invalid: this place is used by other ship.";
            }
        }
        return null;
    }


}

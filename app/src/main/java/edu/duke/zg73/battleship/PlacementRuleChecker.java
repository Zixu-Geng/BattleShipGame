package edu.duke.zg73.battleship;

import java.io.Serializable;

public abstract class PlacementRuleChecker<T> implements Serializable {
    private final PlacementRuleChecker<T> next;
    public PlacementRuleChecker(PlacementRuleChecker<T> next) {
        this.next = next;
    }


    /**
     * run all checkers to check hte validation of the placement
     * @param theShip is the ship to check
     * @param theBoard is the board that the ship will be placed
     * @return error message if it is not valid, null if it is valid
     */

    public String checkPlacement (Ship<T> theShip, Board<T> theBoard) {


        if (checkMyRule(theShip, theBoard) != null) {
            return checkMyRule(theShip, theBoard);
        }


        if (next != null) {
            return next.checkPlacement(theShip, theBoard);
        }

        return null;
    }



    protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);
}

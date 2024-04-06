package edu.duke.zg73.battleship;

import java.io.Serializable;

public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> implements Serializable {

    public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
        super(next);


    }


    /**
     * check the validation of put a ship in a certain board, the will check the coordinate is in the board
     * @param theShip is the ship to check
     * @param theBoard is the borad to display
     * @return
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        // TODO Auto-generated method stub

        int height = theBoard.getHeight();
        int width = theBoard.getWidth();

        for(Coordinate c : theShip.getCoordinates()){
            if(c.getRow()> height-1){
                return "That placement is invalid: the ship goes off the bottom of the board.";
            }
            if(c.getRow()< 0){
                return "That placement is invalid: the ship goes off the top of the board.";
            }
            if(c.getColumn() > (width-1)){
                return "That placement is invalid: the ship goes off the right of the board.";
            }
            if(c.getColumn() < 0){
                return "That placement is invalid: the ship goes off the left of the board.";
            }
        }
        return null;
    }


}




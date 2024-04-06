package edu.duke.zg73.battleship;

import java.io.Serializable;

public class V2ShipFactory implements AbstractShipFactory, Serializable {

    /**
     * create a ship
     * @param where specifies the location and orientation of the ship to make
     * @param w specifies the width
     * @param h specifies the length
     * @param letter specifies the letter to use to represent the ship
     * @param name specifies the name of the ship
     * @return
     */
    protected Ship<Character> createRectangelShip(Placement where, int w, int h, char letter, String name){
        if(where.getOrientation() == 'H'){
            return new RectangleShip<Character>(name, where.getWhere(), h, w, letter, '*');
        }else if (where.getOrientation() == 'V'){
            return new RectangleShip<Character>(name, where.getWhere(), w, h, letter, '*');
        }else{
            throw new IllegalArgumentException("Ship's orientation for "+name+" must be one of 'V', 'H' but is " + where);
        }
    }

    private Ship<Character> createShapedShip(Placement where, char letter, String name){
        if(where.getOrientation() == 'U' || where.getOrientation() == 'R' || where.getOrientation() == 'D' || where.getOrientation() == 'L') {
            return new ShapedShip<Character>(name, where, letter, '*');
        }else{
            throw new IllegalArgumentException("Ship's orientation for "+name+" must be one of 'U', 'R', 'D', 'L' but is " + where);
        }
    }

    @Override
    public Ship makeSubmarine(Placement where) {
        return createRectangelShip(where, 1,2, 's', "Submarine");
    }

    @Override
    public Ship makeDestroyer(Placement where) {
        return createRectangelShip(where, 1, 3, 'd', "Destroyer");
    }

    @Override
    public Ship makeCarrier(Placement where) {
        return createShapedShip(where, 'c', "Carrier");
    }

    @Override
    public Ship makeBattleship(Placement where) {
        return createShapedShip(where, 'b', "Battleship");
    }


}

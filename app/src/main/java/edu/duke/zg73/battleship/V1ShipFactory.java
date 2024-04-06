package edu.duke.zg73.battleship;

import java.io.Serializable;

public class V1ShipFactory implements AbstractShipFactory, Serializable {

    /**
     * create a ship
     * @param where specifies the location and orientation of the ship to make
     * @param w specifies the width
     * @param h specifies the length
     * @param letter specifies the letter to use to represent the ship
     * @param name specifies the name of the ship
     * @return
     */
    protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name){
        if(where.getOrientation() == 'H'){
            return new RectangleShip<Character>(name, where.getWhere(), h, w, letter, '*');
        }else{
            return new RectangleShip<Character>(name, where.getWhere(), w, h, letter, '*');
        }


    }

    /**
     * make a submarine
     * @param where specifies the location and orientation of the ship to make
     * @return
     */
    @Override
    public Ship makeSubmarine(Placement where) {

        return createShip(where, 1,2, 's', "Submarine");
    }

    /**
     * make a battleship
     * @param where specifies the location and orientation of the ship to make
     * @return
     */
    @Override
    public Ship makeBattleship(Placement where) {

        return createShip(where, 1, 4, 'b', "Battleship");
    }

    /**
     * make a carrier
     * @param where specifies the location and orientation of the ship to make
     * @return
     */
    @Override
    public Ship makeCarrier(Placement where) {

        return createShip(where, 1, 6, 'c', "Carrier");
    }

    /**
     * make a destroyer
     * @param where specifies the location and orientation of the ship to make
     * @return
     */
    @Override
    public Ship makeDestroyer(Placement where) {

        return createShip(where, 1, 3, 'd', "Destroyer");
    }
}

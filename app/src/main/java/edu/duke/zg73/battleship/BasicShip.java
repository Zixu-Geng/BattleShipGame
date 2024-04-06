package edu.duke.zg73.battleship;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.System.out;

public abstract class BasicShip<T> implements Ship<T>, Serializable {

    protected ShipDisplayInfo<T> myDisplayInfo;

    protected ShipDisplayInfo<T> enemyDisplayInfo;
    protected final HashMap<Coordinate, Boolean> myPieces;

    protected final HashMap<Coordinate, Character> root_case;

    protected HashSet<Coordinate> hid_hit;



    /**
     * Constructs a BasicSHip with the specified width and displayinfo
     * @param where is the coordinate in the board
     * @param myDisplayInfo is the displayinfo such as Mydata and OnHit
     */
    public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo){
        this.myPieces = new HashMap<Coordinate, Boolean>();
        this.root_case = new HashMap<Coordinate, Character>();
        this.myDisplayInfo = myDisplayInfo;
        this.enemyDisplayInfo = enemyDisplayInfo;
        for(Coordinate c: where){
            this.myPieces.put(c, false);
        }

        this.hid_hit = new HashSet<>();
    }


    /**
     * Check if this ship occupies the given coordinate.
     *
     * @param where is the Coordinate to check if this Ship occupies
     * @return true if where is inside this ship, false if not.
     */
    @Override
    public boolean occupiesCoordinates(Coordinate where) {
        return this.myPieces.get(where) != null;

    }

    /**
     * Check if this ship has been hit in all of its locations meaning it has been
     * sunk.
     *
     * @return true if this ship has been sunk, false otherwise.
     */
    @Override
    public boolean isSunk(){
        for (boolean part : myPieces.values()) {
            if (part == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Make this ship record that it has been hit at the given coordinate. The
     * specified coordinate must be part of the ship.
     *
     * @param where specifies the coordinates that were hit.
     * @throws IllegalArgumentException if where is not part of the Ship
     */
    @Override
    public void recordHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        if(hid_hit.contains(where)){
            edit_hidhit(where, false);
        }
        this.myPieces.put(where, true);
    }

    /**
     * Check if this ship was hit at the specified coordinates. The coordinates must
     * be part of this Ship.
     *
     * @param where is the coordinates to check.
     * @return true if this ship as hit at the indicated coordinates, and false
     *         otherwise.
     * @throws IllegalArgumentException if the coordinates are not part of this
     *                                  ship.
     */
    @Override
    public boolean wasHitAt(Coordinate where) {

        checkCoordinateInThisShip(where);
        return this.myPieces.get(where);
    }


    /**
     * Return the view-specific information at the given coordinate. This coordinate
     * must be part of the ship.
     *
     * @param where is the coordinate to return information for
     * @param myShip is the bool indiate is it myship
     * @throws IllegalArgumentException if where is not part of the Ship
     * @return The view-specific information at that coordinate.
     */
    @Override
    public T getDisplayInfoAt(Coordinate where, boolean myShip) {



        if(!root_case.isEmpty() && myShip == true && this.wasHitAt(where) == false){
            if(root_case.containsKey(where)){
                return (T) root_case.get(where);
            }

        }

        checkCoordinateInThisShip(where);
        if(myShip){

            return this.myDisplayInfo.getInfo(where, this.wasHitAt(where));
        }else{
            if(hid_hit.contains(where)){
                return this.enemyDisplayInfo.getInfo(where, false);
            }
            return this.enemyDisplayInfo.getInfo(where, this.wasHitAt(where));
        }

    }

    /**
     * check is there a part of ship in a specified coordinate
     * @param c is the coordiante to check
     */
    protected void checkCoordinateInThisShip(Coordinate c){
        if(this.myPieces.get(c) == null){
            throw new IllegalArgumentException("not a part of ship");
        }
    }

    /**
     * Get all of the Coordinates that this Ship occupies.
     * @return An Iterable with the coordinates that this Ship occupies
     */
    public Iterable<Coordinate> getCoordinates(){
        return myPieces.keySet();
    }





}
				  


package edu.duke.zg73.battleship;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public interface Board<T> extends Serializable {
    /**
     * getter of the width
     * @return width
     */
    public int getWidth();

    /**
     * getter of the height
     * @return height
     */
    public int getHeight();




    /**
     * get the displayinfo at a specified coordinate
     * @param where is the coordinate in the board
     * @return the charater in the board
     */
    T whatIsAtForSelf(Coordinate where);

    /**
     * get the displayinfo at a specified coordiate for enemy
     * @param where is the coordinate in the board
     * @return the displayinfo
     */
    T whatIsAtForEnemy(Coordinate where);

    /**
     * try to add ships into myShips, it will call checker to test valid before add
     * @param toAdd is the ship to add
     * @return null if add success, else return "invalid ship to add"
     */
    String tryAddShip(Ship<T> toAdd);


    /**
     * Fire at a specified coordinate
     * @param c is the coordinate to fire
     * @return ship
     */
    Ship<T> fireAt(Coordinate c);

    boolean is_lost();

    ArrayList<Ship<T>> get_avaliable();

    void moveShip(Ship<T> oldShip, Placement newPlacement);

    ArrayList<Ship<T>> get_myShips();

    public HashSet<Coordinate> get_enemyMiss();


}

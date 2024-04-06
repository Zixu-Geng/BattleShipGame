package edu.duke.zg73.battleship;

import java.io.Serializable;

public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo, Serializable {
    private final T myData;
    private final T onHit;


    /**
     * Constructors of ship view info
     * @param myData, display info for ship
     * @param onHit, display info for hit ship
     */
    public SimpleShipDisplayInfo(T myData, T onHit) {
        this.myData = myData;
        this.onHit = onHit;
    }


    /**
     * get the display info of a coordiante whip
     * @param where is the coordinate to check
     * @param hit is whether the ship is hit
     * @return the displyinfo
     */
    @Override
    public T getInfo(Coordinate where, boolean hit) {
        if(hit){
            return onHit;
        }else{
            return myData;
        }

    }
}

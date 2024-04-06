package edu.duke.zg73.battleship;

import java.io.Serializable;

public interface ShipDisplayInfo<T> extends Serializable {
    public T getInfo(Coordinate where, boolean hit);
}

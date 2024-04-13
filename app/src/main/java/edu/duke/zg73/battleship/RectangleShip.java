package edu.duke.zg73.battleship;

import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.HashSet;

public class RectangleShip<T> extends BasicShip<T> implements Serializable {

    private final String name;
    private final Coordinate upperLeft;

    protected final Character orientation;

    public int id;


    /**
     * Construcst that call makeCoords to make the ship
     * @param name,  name of the ship
     * @param upperLeft, upleft coordinate of the ship
     * @param width, width of the ship
     * @param height, height of the ship
     * @param displayInfo, display of ship
     * @param enemydisplayInfo, display of the ship when hit
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, SimpleShipDisplayInfo<T> displayInfo, SimpleShipDisplayInfo<T> enemydisplayInfo) {
        super(makeCoords(upperLeft, width, height), displayInfo, enemydisplayInfo);
        this.name = name;
        this.upperLeft = upperLeft;
        if(width == 1){
            this.orientation = 'V';
        }else{
            this.orientation = 'H';
        }

    }

    /**
     * Constructs of rectangel ship, call another constructs
     * @param name, name of the ship
     * @param upperLeft, upleft coordinate of the ship
     * @param width, width of the ship
     * @param height, height of the ship
     * @param data, display of ship
     * @param onHit, display of the ship when hit
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
        this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    }

    /**
     * Constructs of rectangle, call another constructs
     * @param upperLeft, uppleft coordiante of the ship
     * @param data, display of ship
     * @param onHit, display of the ship when hit
     */
    public RectangleShip(Coordinate upperLeft, T data, T onHit) {
        this("testship", upperLeft, 1, 1, data, onHit);
    }


    static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height){
        HashSet<Coordinate> coords = new HashSet<>();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                coords.add(new Coordinate(upperLeft.getRow() + j, upperLeft.getColumn() + i));
            }
        }

        return coords;
    }

    /**
     * Get the name of this Ship, such as "submarine".
     * @return the name of this ship
     */
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Coordinate getUpperLeft() {
        return this.upperLeft;
    }

    @Override
    public Character getOrientation() {
        return this.orientation;
    }



    @Override
    public void edit_hidhit(Coordinate where, boolean isadd) {
        if(isadd){
            hid_hit.add(where);
        }else{
            hid_hit.remove(where);
        }

    }


}







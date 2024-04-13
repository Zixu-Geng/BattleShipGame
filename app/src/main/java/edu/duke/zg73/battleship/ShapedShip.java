package edu.duke.zg73.battleship;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

public class ShapedShip<T> extends BasicShip<T> implements Serializable {
    private final String name;
    private final Coordinate upperleft;

    protected final Character orientation;

    public int id;

    /**
     * Constructs a shaped ship with the specified name, placement, and display information.
     * @param name the name of the ship
     * @param where the placement of the ship
     * @param myDisplayInfo the display information for the ship
     * @param enemyDisplayInfo the display information for the ship when it has been hit
     */
    public ShapedShip(String name, Placement where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        super(makeCoords(where, name), myDisplayInfo, enemyDisplayInfo);
        this.name = name;
        this.upperleft = where.getWhere();
        this.orientation = where.getOrientation();
        if(name == "Battleship") {
            if(where.getOrientation() == 'R' || where.getOrientation() == 'D'){
                this.root_case.put(where.getWhere(), 'B');
            }
        }else{
            if(where.getOrientation() == 'U' || where.getOrientation() == 'D'){
                this.root_case.put(where.getWhere(), 'C');
            }
        }



    }

    /**
     * Constructs a shaped ship with the specified name, placement, and display information.
     * @param name the name of the ship
     * @param where the placement of the ship
     * @param data the display information for the ship
     * @param onHit the display information for the ship when it has been hit
     */
    public ShapedShip(String name, Placement where, T data, T onHit) {
        this(name, where, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    }

    /**
     * Constructs a shaped ship with the specified placement and display information.
     * @param where the placement of the ship
     * @param data the display information for the ship
     * @param onHit the display information for the ship when it has been hit
     */
    public ShapedShip(Placement where, T data, T onHit) {
        this("Battleship", where, data, onHit);
    }

    /**
     * Constructs a shaped ship with the specified placement and display information.
     * @param where the placement of the ship
     * @param name the name of the ship
     * @return
     */
    static HashSet<Coordinate> makeCoords(Placement where, String name) {

        HashSet<Coordinate> coords = new HashSet<>();
        if (Objects.equals(name, "Battleship")) {
            if (where.getOrientation() == 'U') {

                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 2));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 1));

            } else if (where.getOrientation() == 'R') {
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 2, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 1));
            } else if (where.getOrientation() == 'D') {
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 2));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 1));
            } else{

                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 2, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn()));
            }
        } else {
            if (where.getOrientation() == 'U') {
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 2, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 3, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 2, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 3, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 4, where.getWhere().getColumn() + 1));
            } else if (where.getOrientation() == 'R') {
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 2));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 3));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 4));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 2));
            } else if (where.getOrientation() == 'D') {
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 2, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 2, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 3, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 4, where.getWhere().getColumn() + 1));
            } else{
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 2));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 3));
                coords.add(new Coordinate(where.getWhere().getRow(), where.getWhere().getColumn() + 4));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn()));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 1));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 2));
                coords.add(new Coordinate(where.getWhere().getRow() + 1, where.getWhere().getColumn() + 3));
            }
        }
        return coords;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Coordinate getUpperLeft() {
        return this.upperleft;
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

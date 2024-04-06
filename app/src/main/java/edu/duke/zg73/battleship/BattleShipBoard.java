package edu.duke.zg73.battleship;

import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.System.out;


public class BattleShipBoard<T> implements Board<T>, Serializable {
    private final int width;
    private final int height;

    private final PlacementRuleChecker<T> placementChecker;
    private final ArrayList<Ship<T>> myShips;

    public HashSet<Coordinate> enemyMisses;

    final T missInfo;

    public HashMap<Coordinate, T> old_hit;

    /**
     * Constructs a BattleShipBoard with the specified width
     * and height
     * @param width is the width of the newly constructed board.
     * @param height is the height of the newly constructed board.
     * @throws IllegalArgumentException if the width or height are less than or equal to zero.
     */
    public BattleShipBoard(int width, int height, T missInfo){

        this(width, height, new InBoundsRuleChecker<>(new NoCollisionRuleChecker<>(null)), missInfo);



    }

    /**
     * Constructs a BattleShipBoard with the specified width
     * height and checker
     * @param width is the width of the newly constructed board.
     * @param height is the height of the newly constructed board.
     * @param checker is the rule checker of the newly constructed board
     * @throws IllegalArgumentException if the width or height are less than or equal to zero.
     */
    public BattleShipBoard(int width, int height, PlacementRuleChecker<T> checker, T missInfo){
        if (width <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + height);
        }
        this.width = width;
        this.height = height;
        this.myShips = new ArrayList<>();

        this.placementChecker = checker;
        this.enemyMisses = new HashSet<>();

        this.missInfo = missInfo;
        this.old_hit = new HashMap<>();
    }


    /**
     * Getter of field width
     * @return width
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * Getter of field height
     * @return height
     */
    public int getHeight(){
        return this.height;
    }



    /**
     * try to add ships into myShips, it will call checker to test valid before add
     * @param toAdd is the ship to add
     * @return null if add success, else return "invalid ship to add"
     */
    public String tryAddShip(Ship<T> toAdd){

        if(placementChecker.checkPlacement(toAdd, this) == null){
            myShips.add(toAdd);
            return null;
        }

        return "invalid ship to add";
    }

    @Override
    public Ship<T> fireAt(Coordinate c) {
        for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(c)) {
                s.recordHitAt(c);

                if(enemyMisses.contains(c)){
                    enemyMisses.remove(c);
                }
                return s;
            }
        }

        enemyMisses.add(c);

        return null;
    }


    /**
     * get the displayinfo at a specified coordinate
     * @param where is the coordinate in the board
     * @return the charater in the board
     */


    @Override
    public T whatIsAtForEnemy(Coordinate where) {
        return whatIsAt(where,false);
    }


    public T whatIsAtForSelf(Coordinate where) {
        return whatIsAt(where, true);
    }
    protected T whatIsAt(Coordinate where, boolean isSelf){
        if(isSelf == false){

            if(enemyMisses.contains(where)){
                return missInfo;
            }
        }

        if(old_hit.containsKey(where) && isSelf == false){
            return old_hit.get(where);
        }

        for (Ship<T> s: myShips) {
            if (s.occupiesCoordinates(where)){
                return s.getDisplayInfoAt(where, isSelf);
            }
        }

        return null;
    }

    /**
     * check if the player lose the game
     * @return true if lose, else is false
     */
    public boolean is_lost(){
        for (Ship<T> myShip : this.myShips) {
            if (myShip.isSunk() == false) {
                return false;
            }
        }
        return true;
    }


    public ArrayList<Ship<T>>get_avaliable(){
        ArrayList<Ship<T>> avaliable = new ArrayList<>();
        for (Ship<T> myShip : this.myShips) {
            if (myShip.isSunk() == false) {
                avaliable.add(myShip);
            }
        }
        return avaliable;

    }

    public void moveShip(Ship<T> oldShip, Placement newPlacement) {

        myShips.remove(oldShip);
        Ship<T> tempShip = null;
        V2ShipFactory f = new V2ShipFactory();

        if (oldShip.getName().equals("Battleship")) {
            tempShip = f.makeBattleship(newPlacement);
        } else if (oldShip.getName().equals("Submarine")) {
            tempShip = f.makeSubmarine(newPlacement);
        } else if (oldShip.getName().equals("Destroyer")) {
            tempShip = f.makeDestroyer(newPlacement);
        } else { // Assuming default is Carrier
            tempShip = f.makeCarrier(newPlacement);
        }


        int baseOldRow = oldShip.getUpperLeft().getRow();
        int baseOldCol = oldShip.getUpperLeft().getColumn();
        int baseNewRow = newPlacement.getWhere().getRow();
        int baseNewCol = newPlacement.getWhere().getColumn();

        Character oldOrientation = oldShip.getOrientation();
        Character newOrientation = newPlacement.getOrientation();
        Coordinate new_hit = null;

        for (Coordinate oldCoord : oldShip.getCoordinates()) {

            if (oldShip.wasHitAt(oldCoord)) {

                int row = oldCoord.getRow() - baseOldRow;
                int col = oldCoord.getColumn() - baseOldCol;

                if (oldOrientation.equals(newOrientation)) {
                    new_hit = new Coordinate(baseNewRow + row, baseNewCol + col);
                } else if (tempShip.getName().equals("Submarine") || tempShip.getName().equals("Destroyer")) {
                    if (oldOrientation == 'V') {
                        new_hit = new Coordinate(baseNewRow, baseNewCol + row);
                    } else { // 'H'
                        new_hit = new Coordinate(baseNewRow + col, baseNewCol);
                    }
                } else if (tempShip.getName().equals("Battleship")) {
                    if (oldOrientation == 'U') {
                        if (newOrientation == 'R') {
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 1 - row);
                        } else if (newOrientation == 'D') {
                            new_hit = new Coordinate(baseNewRow + 1 - row, baseNewCol + 2 - col);
                        } else { // 'L'
                            new_hit = new Coordinate(baseNewRow + 2 - col, baseNewCol + row);
                        }
                    } else if (oldOrientation == 'R') {
                        if (newOrientation == 'U') {
                            new_hit = new Coordinate(baseNewRow + 1 - col, baseNewCol + row);
                        } else if (newOrientation == 'D') {
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 2 - row);
                        } else { // 'L'
                            new_hit = new Coordinate(baseNewRow + 2 - row, baseNewCol + 1 - col);
                        }
                    } else if (oldOrientation == 'D') {
                        if (newOrientation == 'U') {
                            new_hit = new Coordinate(baseNewRow + 1 - row, baseNewCol + 2 - col);
                        } else if (newOrientation == 'R') {
                            new_hit = new Coordinate(baseNewRow + 2 - col, baseNewCol + row);
                        } else { // 'L'
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 1 - row);
                        }
                    } else { // 'L'
                        if (newOrientation == 'U') {
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 2 - row);
                        } else if (newOrientation == 'R') {
                            new_hit = new Coordinate(baseNewRow + 2 - row, baseNewCol + 1 - col);
                        } else { // 'D'
                            new_hit = new Coordinate(baseNewRow + 1 - col, baseNewCol + row);
                        }
                    }
                }else {
                    if (oldOrientation == 'U') {
                        if (newOrientation == 'R') {
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 4 - row);
                        } else if (newOrientation == 'D') {
                            new_hit = new Coordinate(baseNewRow + 4 - row, baseNewCol + 1 - col);
                        } else {
                            new_hit = new Coordinate(baseNewRow + 1 - col, baseNewCol + row);
                        }
                    } else if (oldOrientation == 'R') {
                        if (newOrientation == 'U') {
                            new_hit = new Coordinate(baseNewRow + 4 - col, baseNewCol + row);
                        } else if (newOrientation == 'D') {
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 1 - row);
                        } else{
                            new_hit = new Coordinate(baseNewRow + 1 - row, baseNewCol + 4 - col);
                        }
                    } else if (oldOrientation == 'D') {
                        if (newOrientation == 'U') {
                            new_hit = new Coordinate(baseNewRow + 4 - row, baseNewCol + 1 - col);
                        } else if (newOrientation == 'R') {
                            new_hit = new Coordinate(baseNewRow + 1 - col, baseNewCol + row);
                        } else{
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 4 - row);
                        }
                    } else { // 'L'
                        if (newOrientation == 'U') {
                            new_hit = new Coordinate(baseNewRow + col, baseNewCol + 1 - row);
                        } else if (newOrientation == 'R') {
                            new_hit = new Coordinate(baseNewRow + 1 - row, baseNewCol + 4 - col);
                        } else {
                            new_hit = new Coordinate(baseNewRow + 4 - col, baseNewCol + row);
                        }
                    }
                }

                tempShip.recordHitAt(new_hit);
                old_hit.put(oldCoord, oldShip.getDisplayInfoAt(oldCoord, false));
                tempShip.edit_hidhit(new_hit, true);


            }
        }

        String err = tryAddShip(tempShip);

        if (err != null) {
            throw new IllegalArgumentException(err);
        }
    }

    @Override
    public ArrayList<Ship<T>> get_myShips() {
        return this.myShips;
    }

    @Override
    public HashSet<Coordinate> get_enemyMiss() {
        return this.enemyMisses;
    }


}

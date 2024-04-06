package edu.duke.zg73.battleship;

import java.io.Serializable;
import java.util.Objects;

import static java.lang.System.out;

public class Placement implements Serializable {
    private final Coordinate where;
    private final char orientation;

    /**
     * Constructs a Placement with specified Coordinate and orientation
     * @param where is the coordinate of the upperleft
     * @param orientation is the orientation of the placement: H is Horizontal and V is vertical
     * @throws IllegalArgumentException if the orientation is netierh H, V, h, v
     */
    public Placement(Coordinate where, char orientation){
        this.where = where;
        char input_upper = Character.toUpperCase(orientation);
        if(input_upper != 'H' && input_upper != 'V' && input_upper != 'U' && input_upper != 'R' && input_upper != 'D'&& input_upper != 'L') {
            throw new IllegalArgumentException("invalid orientatin format");
        }
        this.orientation = Character.toUpperCase(orientation);
    }


    /**
     * Constructs a Placement with specified Input String
     * @param input is the string contains the orientation and coordinate. Ex: B2V indiates row B, column 2 and Vertical
     * @throws  IllegalArgumentException if the input String has invalid format
     */
    public Placement(String input) {

        if (input == null || input.length() != 3) {

            throw new IllegalArgumentException("length of input should be 3");
        }

        char input_upper = Character.toUpperCase(input.charAt(input.length()-1));
        if(input_upper != 'H' && input_upper != 'V' && input_upper != 'U' && input_upper != 'R' && input_upper != 'D'&& input_upper != 'L') {
            throw new IllegalArgumentException("invalid orientation format");
        }

        this.where = new Coordinate(input.substring(0, input.length() - 1));

        this.orientation = Character.toUpperCase(input.charAt(input.length() - 1));

    }

    /**
     * Getter of where
     * @return where
     */
    public Coordinate getWhere() {
        return this.where;
    }

    /**
     * Getter of orientation
     * @return orientation
     */
    public char getOrientation() {
        return this.orientation;
    }

    /**
     * ToSring()
     * @return String
     */
    @Override
    public String toString() {
        return "Placement{" +
                "where=" + where +
                ", orientation=" + orientation +
                '}';
    }

    /**
     * Equals
     * @param o objects to compare
     * @return Ture if equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placement placement = (Placement) o;
        return orientation == placement.orientation && Objects.equals(where, placement.where);
    }

    /**
     * Hash convert
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(where, orientation);
    }
}

package edu.duke.zg73.battleship;

import java.io.Serializable;
import java.util.Objects;

import static java.lang.System.out;

public class Coordinate implements Serializable {
    private final int row;
    private final int column;

    /**
     * Constructs a coordinate with the specified row and column
     * @param row is the row of the coordiante
     * @param column is the column of the coordiante
     */
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Construcst a coordiante with the specified input String
     * @param str is the input string that contains the required info: B2 indiate row is B and column is 2
     * @throws IllegalArgumentException if input String is invalid
     */
    public Coordinate(String str){
        if(str != null && str.length() == 2 ){
            if (!(Character.isDigit(str.charAt(1)))) {
                throw new IllegalArgumentException("Second Input must be Number !");
            }

            if(Character.toUpperCase(str.charAt(0)) < 'A' || Character.toUpperCase(str.charAt(0))> 'Z'){

                throw new IllegalArgumentException("invalid row");
            }

            this.row = Character.toUpperCase(str.charAt(0))- 'A';
            this.column = Integer.parseInt(str.substring(1));

        }else{

            throw new IllegalArgumentException("Invalid input format");
        }




    }

    /**
     * Getter of row
     * @return row
     */
    public int getRow() {
        return this.row;
    }


    /**
     * Getter of Column
     * @return column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * check whether this equals to object o
     * @param o is the object to compare
     * @return true if equal, else return false
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Coordinate c = (Coordinate) o;
            return row == c.row && column == c.column;
        }
        return false;
    }

    /**
     * ToString
     * @return String
     */
    @Override
    public String toString() {
        return "("+row+", " + column+")";
    }

    /**
     * Hash code
     * @return hash code of the project
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}

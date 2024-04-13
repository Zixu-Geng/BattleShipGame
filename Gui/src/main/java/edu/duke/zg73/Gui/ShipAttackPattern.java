package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.Coordinate;

import java.util.HashSet;
import java.util.ArrayList;

abstract class ShipAttackPattern {
    abstract ArrayList<Coordinate> generateAttackTargets(HashSet<Coordinate> hits);
}
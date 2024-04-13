package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.Coordinate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

class SubmarineAttackPattern extends ShipAttackPattern {

    @Override
    ArrayList<Coordinate> generateAttackTargets(HashSet<Coordinate> hits) {
        return generateSubmarineAttackTargets(hits);
    }


    private ArrayList<Coordinate> generateSubmarineAttackTargets(HashSet<Coordinate> hits) {
        ArrayList<Coordinate> targets = new ArrayList<>();
        if (hits.isEmpty()) {
            return targets;
        }

        int commonRow = hits.iterator().next().getRow();
        int commonColumn = hits.iterator().next().getColumn();
        boolean isHorizontal = hits.stream().allMatch(hit -> hit.getRow() == commonRow);
        boolean isVertical = hits.stream().allMatch(hit -> hit.getColumn() == commonColumn);
        if (isHorizontal) {
            int minColumn = hits.stream().min(Comparator.comparingInt(Coordinate::getColumn)).get().getColumn();
            int maxColumn = hits.stream().max(Comparator.comparingInt(Coordinate::getColumn)).get().getColumn();
            targets.add(new Coordinate(commonRow, minColumn - 1));
            targets.add(new Coordinate(commonRow, maxColumn + 1));
        } else if (isVertical) {
            int minRow = hits.stream().min(Comparator.comparingInt(Coordinate::getRow)).get().getRow();
            int maxRow = hits.stream().max(Comparator.comparingInt(Coordinate::getRow)).get().getRow();
            targets.add(new Coordinate(minRow - 1, commonColumn));
            targets.add(new Coordinate(maxRow + 1, commonColumn));
        }

        return targets;
    }


}
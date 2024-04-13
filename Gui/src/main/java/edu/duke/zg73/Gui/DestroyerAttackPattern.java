package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.Coordinate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class DestroyerAttackPattern extends ShipAttackPattern {
    @Override
    ArrayList<Coordinate> generateAttackTargets(HashSet<Coordinate> hits) {
        return generateDestroyerShapeAttacktargets(hits);
    }

    private ArrayList<Coordinate> generateDestroyerShapeAttacktargets(HashSet<Coordinate> hits) {
        ArrayList<Coordinate> targets = new ArrayList<>();
        if (hits.isEmpty()) {
            return targets;
        }

        // Determine if the destroyer is horizontal or vertical
        boolean isHorizontal = allSameRow(hits);
        boolean isVertical = !isHorizontal && allSameColumn(hits); // Only check column if not horizontal

        if (isHorizontal) {
            Coordinate minCoord = hits.stream().min(Comparator.comparingInt(Coordinate::getColumn)).orElse(null);
            Coordinate maxCoord = hits.stream().max(Comparator.comparingInt(Coordinate::getColumn)).orElse(null);
            if (minCoord != null && maxCoord != null) {
                targets.add(new Coordinate(minCoord.getRow(), minCoord.getColumn() - 1));
                targets.add(new Coordinate(maxCoord.getRow(), maxCoord.getColumn() + 1));
            }
        } else if (isVertical) {
            Coordinate minCoord = hits.stream().min(Comparator.comparingInt(Coordinate::getRow)).orElse(null);
            Coordinate maxCoord = hits.stream().max(Comparator.comparingInt(Coordinate::getRow)).orElse(null);
            if (minCoord != null && maxCoord != null) {
                targets.add(new Coordinate(minCoord.getRow() - 1, minCoord.getColumn()));
                targets.add(new Coordinate(maxCoord.getRow() + 1, minCoord.getColumn()));
            }
        }

        return targets;
    }


    private boolean allSameRow(HashSet<Coordinate> hits) {
        int commonRow = hits.iterator().next().getRow();
        return hits.stream().allMatch(hit -> hit.getRow() == commonRow);
    }

    private boolean allSameColumn(HashSet<Coordinate> hits) {
        int commonColumn = hits.iterator().next().getColumn();
        return hits.stream().allMatch(hit -> hit.getColumn() == commonColumn);
    }
}

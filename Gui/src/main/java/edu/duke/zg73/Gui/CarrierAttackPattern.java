package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.Coordinate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class CarrierAttackPattern extends ShipAttackPattern {
    @Override
    ArrayList<Coordinate> generateAttackTargets(HashSet<Coordinate> hits) {
        return generateCarrierAttackTargets(hits);
    }

    private ArrayList<Coordinate> generateCarrierAttackTargets(HashSet<Coordinate> hits) {
        ArrayList<Coordinate> targets = new ArrayList<>();
        if (hits.isEmpty()) {
            return targets;
        }

        // Determine the bounds of the hits to estimate the orientation and part of the carrier
        int minRow = hits.stream().min(Comparator.comparingInt(Coordinate::getRow)).get().getRow();
        int maxRow = hits.stream().max(Comparator.comparingInt(Coordinate::getRow)).get().getRow();
        int minCol = hits.stream().min(Comparator.comparingInt(Coordinate::getColumn)).get().getColumn();
        int maxCol = hits.stream().max(Comparator.comparingInt(Coordinate::getColumn)).get().getColumn();

        // Infer the possible shape and generate targets based on the number of hits
        if (hits.size() >= 4) {
            inferCarrierShape(hits, targets, minRow, maxRow, minCol, maxCol);
        } else {
            // For fewer hits, consider surrounding potential targets
            for (Coordinate hit : hits) {
                addSurroundingTargets(hit, targets);
            }
        }

        return targets;
    }

    private void addSurroundingTargets(Coordinate hit, ArrayList<Coordinate> targets) {
        int row = hit.getRow();
        int col = hit.getColumn();
        targets.add(new Coordinate(row - 1, col)); // North
        targets.add(new Coordinate(row + 1, col)); // South
        targets.add(new Coordinate(row, col - 1)); // West
        targets.add(new Coordinate(row, col + 1)); // East
    }

    private void inferCarrierShape(HashSet<Coordinate> hits, ArrayList<Coordinate> targets, int minRow, int maxRow, int minCol, int maxCol) {
        // Attempt to determine if we have a vertical configuration
        if ((maxRow - minRow >= 4) && (maxCol - minCol >= 1)) {
            // Possible vertical carrier with extension to the right on the bottom
            targets.add(new Coordinate(minRow - 1, minCol)); // Above the topmost part
            targets.add(new Coordinate(maxRow + 1, minCol)); // Below the bottom part
            targets.add(new Coordinate(maxRow + 1, minCol + 1)); // Extension below the bottom part to the right
        } else {
            // General case, add targets around all known hits
            for (Coordinate hit : hits) {
                addSurroundingTargets(hit, targets);
            }
        }
    }
}

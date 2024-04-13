package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BattleshipAttackPattern extends ShipAttackPattern{
    @Override
    ArrayList<Coordinate> generateAttackTargets(HashSet<Coordinate> hits) {
        return generateBattleshipShapeAttackTargets(hits);
    }


    private ArrayList<Coordinate> generateBattleshipShapeAttackTargets(HashSet<Coordinate> hits) {
        ArrayList<Coordinate> targets = new ArrayList<>();
        if (hits.size() < 2) {
            for (Coordinate hit : hits) {
                targets.addAll(generateSurroundingTargets(hit));
            }
        } else {
            for (Coordinate center : hits) {
                int row = center.getRow();
                int col = center.getColumn();
                boolean couldBeCenter = hits.contains(new Coordinate(row, col + 1))
                        && hits.contains(new Coordinate(row + 1, col));

                if (couldBeCenter) {
                    Coordinate top = new Coordinate(row - 1, col);
                    Coordinate left = new Coordinate(row, col - 1);
                    Coordinate right = new Coordinate(row, col + 2);
                    Coordinate bottom = new Coordinate(row + 2, col);

                    targets.add(top);
                    targets.add(left);
                    targets.add(right);
                    targets.add(bottom);
                }
            }
        }
        return targets;
    }

    private ArrayList<Coordinate> generateSurroundingTargets(Coordinate hit) {
        int row = hit.getRow();
        int col = hit.getColumn();
        ArrayList<Coordinate> targets = new ArrayList<>();
        targets.addAll(Arrays.asList(
                new Coordinate(row - 1, col),
                new Coordinate(row + 1, col),
                new Coordinate(row, col - 1),
                new Coordinate(row, col + 1)
        ));
        return targets;
    }

}
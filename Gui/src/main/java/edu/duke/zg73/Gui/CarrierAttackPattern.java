package edu.duke.zg73.Gui;

import edu.duke.zg73.battleship.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Comparator;

public class CarrierAttackPattern extends ShipAttackPattern {
    @Override
    ArrayList<Coordinate> generateAttackTargets(HashSet<Coordinate> hits) {
        return generateCarrierAttackTargets(hits);
    }

    private ArrayList<Coordinate> generateCarrierAttackTargets(HashSet<Coordinate> hits) {
        ArrayList<Coordinate> targets = new ArrayList<>();
        if (hits.isEmpty()) {
            return targets; // 如果没有击中，返回空列表
        }

        // 使用一个简单的方法来生成周围可能的攻击目标，特别是当击中较少时
        hits.forEach(hit -> {
            // 周围四个方向
            targets.add(new Coordinate(hit.getRow() - 1, hit.getColumn())); // 上
            targets.add(new Coordinate(hit.getRow() + 1, hit.getColumn())); // 下
            targets.add(new Coordinate(hit.getRow(), hit.getColumn() - 1)); // 左
            targets.add(new Coordinate(hit.getRow(), hit.getColumn() + 1)); // 右
            // 对角线方向，因为“L”型结构可能在拐角处
            targets.add(new Coordinate(hit.getRow() - 1, hit.getColumn() - 1)); // 左上
            targets.add(new Coordinate(hit.getRow() - 1, hit.getColumn() + 1)); // 右上
            targets.add(new Coordinate(hit.getRow() + 1, hit.getColumn() - 1)); // 左下
            targets.add(new Coordinate(hit.getRow() + 1, hit.getColumn() + 1)); // 右下
        });

        // 如果击中大于等于4，尝试具体定位“L”形状
        if (hits.size() >= 4) {
            int minRow = hits.stream().min(Comparator.comparingInt(Coordinate::getRow)).get().getRow();
            int maxRow = hits.stream().max(Comparator.comparingInt(Coordinate::getRow)).get().getRow();
            int minCol = hits.stream().min(Comparator.comparingInt(Coordinate::getColumn)).get().getColumn();
            int maxCol = hits.stream().max(Comparator.comparingInt(Coordinate::getColumn)).get().getColumn();
            // 添加可能的“L”型拐角未击中部分
            targets.add(new Coordinate(minRow - 1, maxCol + 1)); // 假设“L”型可能在顶部左侧或右侧
            targets.add(new Coordinate(maxRow + 1, minCol - 1)); // 假设“L”型可能在底部左侧
        }

        return targets;
    }
}

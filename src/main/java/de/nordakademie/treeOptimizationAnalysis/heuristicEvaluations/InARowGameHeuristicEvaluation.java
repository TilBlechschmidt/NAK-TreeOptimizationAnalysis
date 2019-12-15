package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.games.InARowGameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class InARowGameHeuristicEvaluation extends NullSumHeuristicEvaluation<InARowGameState> {
    public static final BinaryOperator<Integer> ACCUMULATE_BY_MAX = (akku,length) -> Math.max(akku,length);
    public static final BinaryOperator<Integer> ACCUMULATE_BY_SQUARE_SUM = (akku, length) -> akku + length  * length;
    public static final PointsCounter.Builder LENGTH_BY_CONSECUTIVE = lengthAccumulator -> (akku, winLength, row) -> {
        int length=0;
        for(Boolean field: row) {
            if(field != null && field) {
                length++;
            } else if(length != 0) {
                akku = lengthAccumulator.apply(akku,length);
                length = 0;
            }
        }
        if(length != 0) {
            akku = lengthAccumulator.apply(akku,length);
        }
        return akku;
    };

    public static final PointsCounter.Builder LENGTH_BY_OBJECTS_IN_POSSIBLE_BOX = lengthAccumulator -> (akku, winLength, row) -> {
        outer: for(int start = 0; start <= row.size() - winLength; start++) {
            int sum = 0;
            for(int index = start; index < start + winLength; index ++) {
                Boolean val = row.get(index);
                if(val != null) {
                    if(val) {
                        sum ++;
                    } else {
                        start = index+1;
                        continue outer;
                    }
                }
            }
            akku = lengthAccumulator.apply(akku, sum);
        }
        return akku;
    };

    public interface PointsCounter {
        interface Builder {
             PointsCounter accumulateBy(BinaryOperator<Integer> lengthAccumulator);
        }
        int applyLengthChanges(int akku, int winLength, List<Boolean> row);
    }

    public static Factory factory(PointsCounter pointsFinder, int maxPoints) {
        return () -> new InARowGameHeuristicEvaluation(0,pointsFinder, maxPoints);
    }

    private int initialAccumulator;
    private PointsCounter lengthDefinition;
    private int maxPoints;

    public InARowGameHeuristicEvaluation(int initialAccumulator, PointsCounter lengthDefinition, int maxPoints) {
        this.initialAccumulator = initialAccumulator;
        this.lengthDefinition = lengthDefinition;
        this.maxPoints = maxPoints;
    }

    @Override
    protected double evalFor(Player player, InARowGameState state) {
        List<List<Boolean>> field = Arrays.stream(state.getField())
                .map(Arrays::stream)
                .map(s -> s == null ? null : s.map(player::equals))
                .map(s -> s.collect(Collectors.toList()))
                .collect(Collectors.toList());
        int winLength = state.getWinLength();
        int accumulator = eval(0,1, field, initialAccumulator, winLength);
        accumulator = eval(1,1, field, accumulator,winLength );
        accumulator = eval(1,0, field, accumulator,winLength);
        accumulator = eval(1,-1, field, accumulator,winLength);
        return accumulator/ maxPoints;
    }

    private int eval(int dx, int dy, List<List<Boolean>> field, int accumulator, int winLength) {
        for(int x = 0; x < field.size(); x ++) {
            for(int y = 0; y < field.get(x).size(); y ++) {
                accumulator = eval(dx,dy,x,y,field, accumulator,winLength);
            }
        }
        return accumulator;
    }

    private int eval(int dx, int dy, int x, int y, List<List<Boolean>> field, int accumulator, int winLength) {
        List<Boolean> bools = new ArrayList<>();
        while(x < field.size() && y > -1 && y < field.get(x).size()) {
            bools.add(field.get(x).get(y));
            x += dx;
            y += dy;
        }
        accumulator = lengthDefinition.applyLengthChanges(accumulator, winLength, bools);
        return accumulator;
    }
}

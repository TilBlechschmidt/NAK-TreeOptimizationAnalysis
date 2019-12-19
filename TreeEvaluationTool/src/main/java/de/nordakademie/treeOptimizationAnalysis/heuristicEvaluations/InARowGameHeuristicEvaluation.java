package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.InARowGameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class InARowGameHeuristicEvaluation extends NullSumHeuristicEvaluation<InARowGameState> {
    public static final BinaryOperator<Integer> ACCUMULATE_BY_MAX = new BinaryOperator<>() {
        @Override
        public Integer apply(Integer akku, Integer length) {
            return Math.max(akku, length);
        }

        @Override
        public String toString() {
            return "max";
        }
    };
    public static final BinaryOperator<Integer> ACCUMULATE_BY_SQUARE_SUM = new BinaryOperator<>() {
        @Override
        public Integer apply(Integer akku, Integer length) {
            return akku + length*length;
        }
        @Override
        public String toString() {
            return "square sum";
        }
    };


    public static final PointsCounter.Builder LENGTH_BY_CONSECUTIVE = lengthAccumulator -> new PointsCounter() {
        @Override
        public int applyLengthChanges(int akku, int winLength, List<Boolean> row) {
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
        }

        @Override
        public String toString() {
            return "consecutive length accumulated as" + lengthAccumulator.toString();
        }
    };

    public static final PointsCounter.Builder LENGTH_BY_OBJECTS_IN_POSSIBLE_BOX = lengthAccumulator -> new PointsCounter() {
        @Override
        public int applyLengthChanges(int akku, int winLength, List<Boolean> row) {
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
        }
        @Override
        public String toString() {
            return "occupied in possible row accumulated as" + lengthAccumulator.toString();
        }
    };

    public interface PointsCounter {
         interface Builder {
             PointsCounter accumulateBy(BinaryOperator<Integer> lengthAccumulator);
        }
        int applyLengthChanges(int akku, int winLength, List<Boolean> row);
    }

    public static Factory<InARowGameState> factory(PointsCounter pointsFinder, int maxPoints) {
        return () -> new InARowGameHeuristicEvaluation(0, pointsFinder, maxPoints);
    }

    private final int initialAccumulator;
    private final PointsCounter lengthDefinition;
    private final int maxPoints;

    public InARowGameHeuristicEvaluation(int initialAccumulator, PointsCounter lengthDefinition, int maxPoints) {
        this.initialAccumulator = initialAccumulator;
        this.lengthDefinition = lengthDefinition;
        this.maxPoints = maxPoints;
    }

    @Override
    public double evalFor(Player player, InARowGameState state) {
        List<List<Boolean>> field = Arrays.stream(state.getField())
                .map(Arrays::stream)
                .map(s -> s.map(f -> f == null ? null : player.equals(f)))
                .map(s -> s.collect(Collectors.toList()))
                .collect(Collectors.toList());
        int winLength = state.getWinLength();
        int accumulator = eval(0,1, field, initialAccumulator, winLength);
        accumulator = eval(1,1, field, accumulator,winLength );
        accumulator = eval(1,0, field, accumulator,winLength);
        accumulator = eval(1,-1, field, accumulator,winLength);
        return ((double) accumulator) / ((double) maxPoints);
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

    @Override
    public String toString() {
        return "InARowGameHeuristicEvaluation(" + lengthDefinition +
                "/" + maxPoints + ')';
    }
}

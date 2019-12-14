package de.nordakademie.treeOptimizationAnalysis.knownReactionPathes;

import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.KnownReactionsPath;
import de.nordakademie.treeOptimizationAnalysis.Player;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CompressedKnownReactionPath<T extends GameState> implements KnownReactionsPath<T> {

    private Map<T,T> paths = new HashMap<>();
    private Map<T,T> invertedSteps = new HashMap<>();

    @Override
    public void cache(T start, T result) {
        invertedSteps.put(result, start);
        if(paths.containsKey(result)) {
            result = paths.get(result);
        }
        paths.put(start, result);
        while(invertedSteps.containsKey(start)) {
            start = invertedSteps.get(start);
            paths.put(start, result);
        }
    }

    @Override
    public boolean isCached(T start) {
        return paths.containsKey(start);
    }

    @Override
    public T get(T start) {
        return paths.get(start);
    }
}

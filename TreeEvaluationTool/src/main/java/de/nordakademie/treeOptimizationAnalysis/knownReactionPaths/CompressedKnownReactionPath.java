package de.nordakademie.treeOptimizationAnalysis.knownReactionPaths;

import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;

import java.util.HashMap;
import java.util.Map;

public class CompressedKnownReactionPath<T extends GameState<T>> implements KnownReactionsPath<T> {

    public static final Factory FACTORY = CompressedKnownReactionPath::new;

    private final Map<T,T> paths = new HashMap<>();
    private final Map<T,T> invertedSteps = new HashMap<>();

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
        if (!isCached(start)) return start;
        return paths.get(start);
    }

    @Override
    public int size() {
        return paths.size();
    }

    @Override
    public String toString() {
        return "CompressedKnownReactionPath";
    }
}

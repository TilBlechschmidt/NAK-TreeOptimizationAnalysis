package de.nordakademie.treeOptimizationAnalysis.knownReactionPaths;

import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;

import java.util.HashMap;
import java.util.Map;

public class UncompressedKnownReactionsPath<T extends GameState> implements KnownReactionsPath<T> {
    private Map<T,T> reactions = new HashMap<>();
    public static final Factory FACTORY = new Factory() {
        @Override
        public <T extends GameState> KnownReactionsPath<T> create() {
            return new UncompressedKnownReactionsPath<>();
        }
    };
    @Override
    public void cache(T start, T result) {
        reactions.put(start,result);
    }

    @Override
    public boolean isCached(T start) {
        return reactions.containsKey(start);
    }

    @Override
    public T get(T start) {
        T result = start;
        while(isCached(result)) {
            result = reactions.get(result);
        }
        return result;
    }

    @Override
    public int size() {
        return reactions.size();
    }

    @Override
    public String toString() {
        return "UncompressedKnownReactionsPath";
    }
}
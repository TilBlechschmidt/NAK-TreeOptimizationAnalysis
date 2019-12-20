package de.nordakademie.treeOptimizationAnalysis.knownReactionPaths;

import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;

public interface KnownReactionsPath<T extends GameState<T>> {
    interface Factory {
        <T extends GameState<T>> KnownReactionsPath<T> create();
    }

    void cache(T start, T result);

    boolean isCached(T start);

    T get(T start);

    int size();
}

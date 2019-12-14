package de.nordakademie.treeOptimizationAnalysis;

public interface KnownReactionsPath<T extends GameState> {
    interface Factory {
        <T extends GameState> KnownReactionsPath<T> create();
    }

    void cache(T start, T result);

    boolean isCached(T start);

    T get(T start);
}

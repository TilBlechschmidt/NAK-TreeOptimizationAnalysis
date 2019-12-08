package de.nordakademie.treeOptimizationAnalysis.knownReactionPathes;

import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.KnownReactionsPath;

import java.util.Map;

public class CompressedKnownReactionPath<T extends GameState> implements KnownReactionsPath<T> {

    private Map<T,T> steps;

    @Override
    public void addStaticCache(T initialState, int startingSteps, int finalSteps) {

    }

    @Override
    public void cache(T start, T result) {

    }

    @Override
    public boolean isCached(T start) {
        return false;
    }

    @Override
    public T get(T start) {
        return null;
    }
}

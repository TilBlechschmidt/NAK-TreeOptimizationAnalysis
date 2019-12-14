package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.GameState;

public class NeutralHeuristicEvaluation<T extends GameState<T>> extends NullSumHeuristicEvaluation<T> {
    @Override
    protected double evalForPlayer0(T state) {
        return 0;
    }
}

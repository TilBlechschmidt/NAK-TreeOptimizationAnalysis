package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.HeuristicEvaluation;

public class NeutralHeuristicEvaluation<T extends GameState> extends NullSumHeuristicEvaluation<T> {
    @Override
    protected double evalForPlayer0(T state) {
        return 0;
    }
}

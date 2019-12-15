package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.Player;

public class NeutralHeuristicEvaluation<T extends GameState<T>> extends NullSumHeuristicEvaluation<T> {
    @Override
    protected double evalFor(Player p, T state) {
        return 0;
    }
}

package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;

public class NeutralHeuristicEvaluation<T extends GameState<T>> extends NullSumHeuristicEvaluation<T> {
    @Override
    protected double evalFor(Player p, T state) {
        return 0;
    }

    @Override
    public String toString() {
        return "0";
    }
}

package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.Player;

import java.util.Map;

public abstract class RuleCompliantHeuristicEvaluation<T extends GameState> implements HeuristicEvaluation<T> {
    @Override
    public Map<Player, Double> eval(T state) {
        if(state.isFinal()) {
            return state.eval();
        } else {
            return evalHeuristic(state);
        }
    }

    protected abstract Map<Player, Double> evalHeuristic(T state);
}

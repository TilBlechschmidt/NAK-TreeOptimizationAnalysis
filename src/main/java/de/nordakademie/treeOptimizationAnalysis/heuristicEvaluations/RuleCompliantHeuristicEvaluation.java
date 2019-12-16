package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.gamePoints.GamePoints;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;

public abstract class RuleCompliantHeuristicEvaluation<T extends GameState> implements HeuristicEvaluation<T> {
    @Override
    public GamePoints eval(T state) {
        if (state.getGameSituation().isFinal()) {
            return state.getGameSituation();
        } else {
            return evalHeuristic(state);
        }
    }

    protected abstract GamePoints evalHeuristic(T state);
}

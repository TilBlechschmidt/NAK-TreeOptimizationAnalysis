package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gamePoints.FixedGamePoints;
import de.nordakademie.treeOptimizationAnalysis.gamePoints.GamePoints;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;

public abstract class NullSumHeuristicEvaluation<T extends GameState<T>> extends RuleCompliantHeuristicEvaluation<T> {

    @Override
    public GamePoints evalHeuristic(T state) {
        double val = evalFor(Player.PLAYER_1, state) - evalFor(Player.PLAYER_2, state);
        return new FixedGamePoints(val, 0 - val);
    }

    protected abstract double evalFor(Player p, T state);
}

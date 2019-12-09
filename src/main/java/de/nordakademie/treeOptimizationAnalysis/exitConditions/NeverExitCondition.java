package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.ExitCondition;
import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.KnownReactionsPath;

public class NeverExitCondition<T extends GameState> implements ExitCondition<T> {
    public NeverExitCondition() {}
    public NeverExitCondition(HeuristicEvaluation<T> h, KnownReactionsPath<T> k) {this();}

    @Override
    public boolean shouldBreak(T gameState) {
        return gameState.isFinal();
    }
}

package de.nordakademie.treeOptimizationAnalysis.BrakingConditions;

import de.nordakademie.treeOptimizationAnalysis.BrakingCondition;
import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.KnownReactionsPath;

public class NeverBrakingCondition<T extends GameState> implements BrakingCondition<T> {
    public NeverBrakingCondition() {}
    public NeverBrakingCondition(HeuristicEvaluation<T> h, KnownReactionsPath<T> k) {this();}

    @Override
    public boolean shouldBreak(T gameState) {
        return gameState.isFinal();
    }
}

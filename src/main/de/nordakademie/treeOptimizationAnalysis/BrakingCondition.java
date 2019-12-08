package de.nordakademie.treeOptimizationAnalysis;

public interface BrakingCondition<T extends GameState> {
    interface Factory {
        <T extends GameState> BrakingCondition<T>
        create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> path);
    }
    boolean shouldBreak(T gameState);
}

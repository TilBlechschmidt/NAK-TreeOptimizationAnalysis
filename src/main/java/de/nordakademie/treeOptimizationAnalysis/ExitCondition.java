package de.nordakademie.treeOptimizationAnalysis;

public interface ExitCondition<T extends GameState> {
    interface Factory {
        <T extends GameState> ExitCondition<T>
        create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> path);
    }
    boolean shouldBreak(T gameState);
}

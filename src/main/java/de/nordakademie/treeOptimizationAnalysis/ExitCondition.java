package de.nordakademie.treeOptimizationAnalysis;

public interface ExitCondition<T extends GameState<T>> {
    interface Factory {
        <T extends GameState<T>> ExitCondition<T>
        create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> path, Player player);
    }
    boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState);
}

package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.*;

public class NeverExitCondition<T extends GameState<T>> implements ExitCondition<T> {
    public NeverExitCondition() {}
    public NeverExitCondition(HeuristicEvaluation<T> h, KnownReactionsPath<T> k) {this();}

    @Override
    public boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState) {
        return gameState.getState().isFinal();
    }
}
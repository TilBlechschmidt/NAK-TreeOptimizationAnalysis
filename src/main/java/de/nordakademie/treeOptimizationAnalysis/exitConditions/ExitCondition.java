package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;

public interface ExitCondition<T extends GameState<T>> {
    interface Factory {
        <T extends GameState<T>> ExitCondition<T>
        create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> path, Player player);
    }
    boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState);
}

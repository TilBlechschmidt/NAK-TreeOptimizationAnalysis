package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;

public class NeverExitCondition<T extends GameState<T>> implements ExitCondition<T> {
    public static final Factory FACTORY = new Factory() {
        @Override
        public <T extends GameState<T>> ExitCondition<T> create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> path, Player player) {
            return new NeverExitCondition<>();
        }
    };

    public NeverExitCondition() {}

    @Override
    public boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState) {
        return gameState.getState().getGameSituation().isFinal();
    }

    @Override
    public String toString() {
        return "NeverExitCondition";
    }
}
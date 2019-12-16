package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;

public class CompareToOtherOptionsByHeuristicExitCondition<T extends GameState<T>> implements  ExitCondition<T> {
    private double difference;
    private KnownReactionsPath<T> cache;
    private HeuristicEvaluation<T> heuristicEvaluation;
    private Player player;

    public static ExitCondition.Factory factory(double difference) {
        return new ExitCondition.Factory() {
            @Override
            public <T extends GameState<T>> ExitCondition<T> create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> path, Player player) {
                return new CompareToOtherOptionsByHeuristicExitCondition<>(difference, path, heuristicEvaluation, player);
            }
        };
    }

    public CompareToOtherOptionsByHeuristicExitCondition(double difference, KnownReactionsPath<T> cache, HeuristicEvaluation<T> heuristicEvaluation, Player player) {
        this.difference = difference;
        this.cache = cache;
        this.heuristicEvaluation = heuristicEvaluation;
        this.player = player;
    }

    @Override
    public boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState) {
        for (GameStateTreeNode<T> node : evaluationBase.getChildren()) {
            T state = node.getState();

            if (cache.isCached(state)) {
                cache.get(state);
            }

            if (heuristicEvaluation.eval(node.getState()).getPlayerScore(player) > heuristicEvaluation.eval(gameState.getState()).getPlayerScore(player)) {
                return true;
            }
        }

        return false;
    }


}

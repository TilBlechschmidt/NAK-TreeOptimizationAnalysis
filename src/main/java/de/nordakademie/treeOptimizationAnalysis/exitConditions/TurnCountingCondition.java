package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;

public class TurnCountingCondition<T extends GameState<T>> implements ExitCondition<T> {
    private int turnsLookedForward;

    public static ExitCondition.Factory factory(int turnsLookedForward) {
        return new ExitCondition.Factory() {
            public <T extends GameState<T>>  ExitCondition<T> create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> knownReactionsPath, Player player){
                return new TurnCountingCondition<>(turnsLookedForward);
            }};
    }


    public TurnCountingCondition(int turnsLookedForward) {
        this.turnsLookedForward = turnsLookedForward;
    }

    @Override
    public boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState) {
        return gameState.getDepth() - evaluationBase.getDepth() >= turnsLookedForward
                || gameState.getState().getGameSituation().isFinal();
    }
}

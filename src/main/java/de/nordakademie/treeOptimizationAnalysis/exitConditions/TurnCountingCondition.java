package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.*;

public class TurnCountingCondition<T extends GameState<T>> implements ExitCondition<T> {
    private int turnsLookedForward;

    public static ExitCondition.Factory factory(int turnsLookedForward) {
        return new ExitCondition.Factory() {
            public <T extends GameState<T>>  ExitCondition<T> create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> knownReactionsPath){
                return new TurnCountingCondition<>(turnsLookedForward);
            }};
    }


    public TurnCountingCondition(int turnsLookedForward) {
        this.turnsLookedForward = turnsLookedForward;
    }

    @Override
    public boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState) {
        return gameState.getDepth() - evaluationBase.getDepth() >= turnsLookedForward
                || gameState.getState().isFinal();
    }
}

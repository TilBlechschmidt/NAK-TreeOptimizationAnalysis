package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OrExitCondition<T extends GameState<T>> implements ExitCondition<T> {
    final Set<ExitCondition<T>> exitConditions;

    public static ExitCondition.Factory factory(ExitCondition.Factory... args) {
        return new Factory() {
            @Override
            public <T extends GameState<T>> ExitCondition<T> create(HeuristicEvaluation<T> heuristicEvaluation, KnownReactionsPath<T> path, Player player) {
                Set<ExitCondition<T>> createdArgs = Arrays.stream(args)
                        .map(arg -> arg.create(heuristicEvaluation, path, player))
                        .collect(Collectors.toSet());
                return new OrExitCondition<>(createdArgs);
            }
        };
    }

    public OrExitCondition(Set<ExitCondition<T>> exitConditions) {
        this.exitConditions = exitConditions;
    }

    @SafeVarargs
    public OrExitCondition(ExitCondition<T>... exitConditions) {
        this(new HashSet<>(Arrays.asList(exitConditions)));
    }
    @Override
    public boolean shouldBreak(GameStateTreeNode<T> evaluationBase, GameStateTreeNode<T> gameState) {
        for(ExitCondition<T> exitCondition:exitConditions) {
            if(exitCondition.shouldBreak(evaluationBase, gameState)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("or(");
        exitConditions.forEach(b::append);
        b.append(")");
        return b.toString();
    }

}

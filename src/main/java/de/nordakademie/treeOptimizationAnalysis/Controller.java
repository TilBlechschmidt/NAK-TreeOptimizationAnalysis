package de.nordakademie.treeOptimizationAnalysis;

import de.nordakademie.treeOptimizationAnalysis.exitConditions.ExitCondition;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;
import de.nordakademie.treeOptimizationAnalysis.traversalIterator.TreeTraversalIterator;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Controller<T extends GameState<T>> {
    private final ExitCondition<T> exitCondition;
    private final TreeTraversalIterator<T> iterator;
    private final HeuristicEvaluation<T> evaluation;
    private final Set<GameStateTreeNode<T>> leafNodes = new HashSet<>();
    private final KnownReactionsPath<T> cache;
    private GameStateTreeNode<T> initialNode;

    public Controller(ExitCondition<T> exitCondition, TreeTraversalIterator<T> iterator, HeuristicEvaluation<T> evaluation, KnownReactionsPath<T> cache) {
        this.exitCondition = exitCondition;
        this.iterator = iterator;
        this.evaluation = evaluation;
        this.cache = cache;
    }

    public boolean next() {
        var node = iterator.pop();

        var gameSituation = node.getState().getGameSituation();
        if (!exitCondition.shouldBreak(initialNode, node) && !gameSituation.isFinal()) {
            node.expand().forEach(iterator::push);
        } else {
            leafNodes.add(node);
        }

        return iterator.isEmpty();
    }

    public GameStateTreeNode<T> choice() {
        return choice(initialNode);
    }

    public GameStateTreeNode<T> nextMove(GameStateTreeNode<T> initialNode) {
        this.initialNode = initialNode;
        iterator.clear();
        iterator.push(this.initialNode);

        while (!next()) {
        }

        return choice();
    }

    private GameStateTreeNode<T> choice(GameStateTreeNode<T> start) {

        Set<GameStateTreeNode<T>> children = start.getChildren();

        if (children.isEmpty()) {
            return start;
        }

        GameStateTreeNode<T> result = children
                .parallelStream()
                //.stream()
                .peek(this::choice)
                .max(Comparator.comparingDouble(node -> getPoints(node, start.getState().getNextChoice())))
                .orElse(start);
        cache.cache(start.getState(), result.getState());
        return result;
    }

    private double getPoints(GameStateTreeNode<T> state, Player player) {
        return evaluation.eval(cache.get(state.getState())).getPlayerScore(player);
    }


}


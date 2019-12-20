package de.nordakademie.treeOptimizationAnalysis;

import de.nordakademie.treeOptimizationAnalysis.exitConditions.ExitCondition;
import de.nordakademie.treeOptimizationAnalysis.gamePoints.GameSituation;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;
import de.nordakademie.treeOptimizationAnalysis.traversalIterator.TreeTraversalIterator;

import java.lang.management.ManagementFactory;
import java.util.Comparator;
import java.util.Set;

public class Controller<T extends GameState<T>> {
    private final ExitCondition<T> exitCondition;
    private final TreeTraversalIterator<T> iterator;
    private final HeuristicEvaluation<T> evaluation;
    private final KnownReactionsPath<T> cache;
    private GameStateTreeNode<T> initialNode;
    private long time;

    public Controller(ExitCondition<T> exitCondition, TreeTraversalIterator<T> iterator, HeuristicEvaluation<T> evaluation, KnownReactionsPath<T> cache) {
        this.exitCondition = exitCondition;
        this.iterator = iterator;
        this.evaluation = evaluation;
        this.cache = cache;
    }

    private boolean next() {
        GameStateTreeNode<T> node = iterator.pop();
        T state = node.getState();

        GameSituation gameSituation = state.getGameSituation();
        if (!exitCondition.shouldBreak(initialNode, node) && !gameSituation.isFinal()) {
            node.expand().forEach(iterator::push);
        }

        return iterator.isEmpty();
    }

    private GameStateTreeNode<T> choice() {
        return choice(initialNode);
    }

    public GameStateTreeNode<T> nextMove(GameStateTreeNode<T> initialNode) {
        this.initialNode = initialNode;
        iterator.clear();
        iterator.push(this.initialNode);
        long time = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();

        boolean running = true;
        while (running) {
            running = !next();
        }

        GameStateTreeNode<T> choice = choice();

        this.time += ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - time;
        return choice;
    }

    private GameStateTreeNode<T> choice(GameStateTreeNode<T> start) {

        Set<GameStateTreeNode<T>> children = start.getChildren();

        if (children.isEmpty()) {
            return start;
        }

        GameStateTreeNode<T> result = children
                //.parallelStream()
                .stream()
                .peek(this::choice)
                .max(Comparator.comparingDouble(node -> getPoints(node, start.getState().getNextChoice())))
                .orElse(start);
        cache.cache(start.getState(), result.getState());
        return result;
    }

    private double getPoints(GameStateTreeNode<T> state, Player player) {
        return evaluation.eval(cache.get(state.getState())).getPlayerScore(player);
    }

    public ExitCondition<T> getExitCondition() {
        return exitCondition;
    }

    public TreeTraversalIterator<T> getIterator() {
        return iterator;
    }

    public HeuristicEvaluation<T> getEvaluation() {
        return evaluation;
    }

    public KnownReactionsPath<T> getCache() {
        return cache;
    }

    public long getTime() {
        return time;
    }
}


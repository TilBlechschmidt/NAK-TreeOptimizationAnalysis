package de.nordakademie.treeOptimizationAnalysis.traversalIterator;

import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;

import java.util.ArrayDeque;
import java.util.Queue;

public class BreadthFirstTreeTraversalIterator<T extends GameState<T>> implements TreeTraversalIterator<T> {
    private Queue<GameStateTreeNode<T>> queue = new ArrayDeque<>();

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public GameStateTreeNode<T> pop() {
        return queue.poll();
    }

    @Override
    public void push(GameStateTreeNode<T> state) {
        queue.add(state);
    }

    @Override
    public void clear() {
        queue.clear();
    }
}

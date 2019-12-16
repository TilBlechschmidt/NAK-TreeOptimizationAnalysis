package de.nordakademie.treeOptimizationAnalysis.traversalIterator;

import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;

import java.util.Stack;

public class DepthFirstTreeTraversalIterator<T extends GameState<T>> implements TreeTraversalIterator<T> {
    private Stack<GameStateTreeNode<T>> stack = new Stack<>();

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public GameStateTreeNode<T> pop() {
        return stack.pop();
    }

    @Override
    public void push(GameStateTreeNode<T> state) {
        stack.push(state);
    }

    @Override
    public void clear() {
        stack.clear();
    }
}

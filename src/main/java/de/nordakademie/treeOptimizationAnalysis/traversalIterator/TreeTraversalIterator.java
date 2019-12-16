package de.nordakademie.treeOptimizationAnalysis.traversalIterator;

import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;

public interface TreeTraversalIterator<T extends GameState<T>> {
    void clear();

    boolean isEmpty();

    GameStateTreeNode<T> pop();

    void push(GameStateTreeNode<T> state);
}

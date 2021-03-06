package de.nordakademie.treeOptimizationAnalysis.gameStates;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameStateTreeNode<T extends GameState<T>> {
    private final T state;
    private final GameStateTreeNode<T> parent;
    private final int depth;
    private final Set<GameStateTreeNode<T>> children = new HashSet<>();
    private boolean expanded;

    public GameStateTreeNode(T state, GameStateTreeNode<T> parent, int depth) {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
    }

    public T getState() {
        return this.state;
    }

    public GameStateTreeNode<T> getParent() {
        return this.parent;
    }

    public int getDepth() {
        return this.depth;
    }

    public Set<GameStateTreeNode<T>> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public void createChild(T nextState) {
        GameStateTreeNode<T> child = new GameStateTreeNode<>(nextState, this, depth + 1);
        children.add(child);
    }

    public Set<GameStateTreeNode<T>> expand() {
        if (!expanded) {
            state.getNextStates().forEach(this::createChild);
            expanded = true;
        }

        return getChildren();
    }
}

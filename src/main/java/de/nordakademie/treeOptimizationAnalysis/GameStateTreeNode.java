package de.nordakademie.treeOptimizationAnalysis;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameStateTreeNode {
    private final GameState state;
    private final GameStateTreeNode parent;
    private final int depth;
    private Set<GameState> children = new HashSet<>();

    public GameStateTreeNode(GameState state) {
        this.state = state;
        this.parent = null;
        this.depth = 0;
    }

    public GameStateTreeNode(GameState state, GameStateTreeNode parent, int depth) {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
    }

    public GameState getState() {
        return this.state;
    }

    public GameStateTreeNode getParent() {
        return this.parent;
    }

    public int getDepth() {
        return this.depth;
    }

    public Set<GameState> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public GameStateTreeNode createChild(GameState nextState) {
        GameStateTreeNode child = new GameStateTreeNode(nextState, this, depth + 1);
        children.add((GameState) child);
        return child;
    }
}

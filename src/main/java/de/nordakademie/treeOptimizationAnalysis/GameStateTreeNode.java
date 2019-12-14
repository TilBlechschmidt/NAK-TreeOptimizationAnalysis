package de.nordakademie.treeOptimizationAnalysis;

public class GameStateTreeNode {
    private final GameState state;
    private final GameStateTreeNode parent;
    private final int depth;

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

    public GameStateTreeNode createChild(GameState nextState) {
        return new GameStateTreeNode(nextState, this, depth + 1);
    }
}

package de.nordakademie.treeOptimizationAnalysis;

public class GameStateTreeNode<T extends GameState<T>> {
    private final T state;
    private final GameStateTreeNode<T> parent;
    private final int depth;

    public GameStateTreeNode(T state) {
        this.state = state;
        this.parent = null;
        this.depth = 0;
    }

    public GameStateTreeNode(T state, GameStateTreeNode<T> parent, int depth) {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
    }

    public GameState getState() {
        return this.state;
    }

    public GameStateTreeNode<T> getParent() {
        return this.parent;
    }

    public int getDepth() {
        return this.depth;
    }

    public GameStateTreeNode createChild(GameState nextState) {
        return new GameStateTreeNode(nextState, this, depth + 1);
    }
}

package de.nordakademie.treeOptimizationAnalysis;

public enum Player {
    PLAYER_1(1),
    PLAYER_2(2);

    private int index;

    Player(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

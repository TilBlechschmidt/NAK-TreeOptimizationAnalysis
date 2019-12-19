package de.nordakademie.treeOptimizationAnalysis;

public enum Player {
    PLAYER_1(1),
    PLAYER_2(2);

    private final int index;

    Player(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Player getOther() {
        Player[] values = Player.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}

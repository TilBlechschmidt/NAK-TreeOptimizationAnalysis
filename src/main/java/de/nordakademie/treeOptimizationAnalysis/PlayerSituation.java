package de.nordakademie.treeOptimizationAnalysis;

public enum PlayerSituation {
    HasWon(1),
    Running(0),
    Tie(-0),
    HasLost(-1);

    private double stateValue;

    PlayerSituation(double stateValue) {
        this.stateValue = stateValue;
    }

    public double getStateValue() {
        return stateValue;
    }
}

package de.nordakademie.treeOptimizationAnalysis;

import java.util.Map;
import java.util.Set;

enum PlayerSituation {
    HasWon(1),
    Running(0),
    Deadlock(-1);

    private double stateValue;

    PlayerSituation(double stateValue) {
        this.stateValue = stateValue;
    }

    public double getStateValue() {
        return stateValue;
    }
}

public interface GameState<T extends GameState<T>> {
    Map<Player, PlayerSituation> getGameSituation();

    Set<T> getNextStates();

    Player getNextChoice();

    // TODO Remove these deprecated methods
    boolean isFinal();

    Map<Player, Double> eval();
}

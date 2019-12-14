package de.nordakademie.treeOptimizationAnalysis;

import java.util.Map;
import java.util.Set;

public interface GameState<T extends GameState<T>> {
    boolean isFinal();
    Map<Player,Double> eval();
    Set<T> getNextStates();
    Player getNextChoice();
}

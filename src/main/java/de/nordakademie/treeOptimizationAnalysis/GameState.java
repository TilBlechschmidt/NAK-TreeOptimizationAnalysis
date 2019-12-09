package de.nordakademie.treeOptimizationAnalysis;

import java.util.List;
import java.util.Map;

public interface GameState<T extends GameState<T>> {
    List<GameState<T>> getNextStates();
    boolean isFinal();
    Map<Player,Double> eval();
    GameState<T> getParent();
    // TODO getTurnCount -> why return T?
}

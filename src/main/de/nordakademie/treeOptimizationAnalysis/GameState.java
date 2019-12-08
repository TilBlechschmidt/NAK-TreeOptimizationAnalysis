package de.nordakademie.treeOptimizationAnalysis;

import java.util.Map;

public interface GameState<T extends GameState<T>> {
    boolean isFinal();
    Map<Player,Double> eval();


}

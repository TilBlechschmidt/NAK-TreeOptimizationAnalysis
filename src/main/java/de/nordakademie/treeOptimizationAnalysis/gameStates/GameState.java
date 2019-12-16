package de.nordakademie.treeOptimizationAnalysis.gameStates;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gamePoints.GameSituation;

import java.util.Set;

public interface GameState<T extends GameState<T>> {
    GameSituation getGameSituation();

    Set<T> getNextStates();

    Player getNextChoice();
}

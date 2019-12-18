package de.nordakademie.treeOptimizationAnalysis.gamePoints;

import de.nordakademie.treeOptimizationAnalysis.Player;

public interface GamePoints {
    double getPlayer1Score();

    double getPlayer2Score();

    default double getPlayerScore(Player player) {
        switch (player) {
            case PLAYER_1:
                return getPlayer1Score();
            case PLAYER_2:
                return getPlayer2Score();
            default:
                return 0;
        }
    }
}


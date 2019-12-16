package de.nordakademie.treeOptimizationAnalysis.gamePoints;

import de.nordakademie.treeOptimizationAnalysis.Player;

public enum GameSituation implements GamePoints {
    WON_PLAYER1(1, -1, true, "Won: P1"),
    WON_PLAYER2(-1, 1, true, "Won: P2"),
    TIE(0, 0, true, "Tie"),
    RUNNING(-0.1, -0.1, false, "Running");

    private double player1Score;
    private double player2Score;
    private boolean isFinal;
    private String description;

    GameSituation(double player1Score, double player2Score, boolean isFinal, String description) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.isFinal = isFinal;
        this.description = description;
    }

    public static GameSituation hasWon(Player player) {
        switch (player) {
            case PLAYER_1:
                return WON_PLAYER1;
            case PLAYER_2:
                return WON_PLAYER2;
            default:
                return null;
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public double getPlayer1Score() {
        return player1Score;
    }

    @Override
    public double getPlayer2Score() {
        return player2Score;
    }

    @Override
    public String toString() {
        return this.description;
    }
}

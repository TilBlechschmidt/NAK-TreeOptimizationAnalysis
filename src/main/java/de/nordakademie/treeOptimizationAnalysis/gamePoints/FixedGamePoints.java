package de.nordakademie.treeOptimizationAnalysis.gamePoints;

public class FixedGamePoints implements GamePoints {
    private double player1Score;
    private double player2Score;

    public FixedGamePoints(double player1Score, double player2Score) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
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
        return "FixedGamePoints{" +
                "player1:" + player1Score +
                ", player2:" + player2Score +
                '}';
    }
}

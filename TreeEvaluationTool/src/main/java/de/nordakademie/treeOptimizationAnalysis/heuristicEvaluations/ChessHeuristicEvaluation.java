package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.games.ChessGameState;
import de.nordakademie.treeOptimizationAnalysis.games.ChessPiece;

import java.util.Arrays;
import java.util.Objects;

public class ChessHeuristicEvaluation extends NullSumHeuristicEvaluation<ChessGameState> {
    @Override
    public double evalFor(Player player, ChessGameState state) {
        return ((double)Arrays.stream(state.getBoard())
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(chessPiece -> chessPiece.getPlayer().equals(player))
                .mapToInt(ChessPiece::getValue)
                .sum())/40.0;
    }
}

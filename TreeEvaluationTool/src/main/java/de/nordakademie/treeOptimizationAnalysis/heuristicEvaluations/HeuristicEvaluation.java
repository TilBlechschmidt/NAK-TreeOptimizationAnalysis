package de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations;

import de.nordakademie.treeOptimizationAnalysis.gamePoints.GamePoints;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;

/**
 * A Heuristic Evaluation is a simple and fast evaluation of how good is a state.
 * It is used only if the braking-condition says that the recursive approach is to be broken.
 * @param <T>
 */
public interface HeuristicEvaluation<T extends GameState<T>> {

    interface Factory<T extends GameState<T>> {
        HeuristicEvaluation<T> create();
    }

    GamePoints eval(T state);
}

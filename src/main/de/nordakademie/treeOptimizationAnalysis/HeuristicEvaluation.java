package de.nordakademie.treeOptimizationAnalysis;

import java.util.Map;

/**
 * A Heuristic Evaluation is an better performent evaluation of how good is a state.
 * It is used only if the braking-condition says that the recursiv approach is to be broken.
 * @param <T>
 */
public interface HeuristicEvaluation<T extends GameState> {

    interface Factory<T extends GameState<T>> {
        HeuristicEvaluation<T> create();
    }

    Map<Player, Double> eval(T state);
}

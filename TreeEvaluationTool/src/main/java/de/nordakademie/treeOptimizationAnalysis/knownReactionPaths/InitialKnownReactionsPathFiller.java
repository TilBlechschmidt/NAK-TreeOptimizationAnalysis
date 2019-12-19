package de.nordakademie.treeOptimizationAnalysis.knownReactionPaths;

import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InitialKnownReactionsPathFiller {

    private final int initialSteps;
    private final int finalSteps;
    private final Player ownPlayer;

    public InitialKnownReactionsPathFiller(int initialSteps, int finalSteps, Player ownPlayer) {
        this.initialSteps = initialSteps;
        this.finalSteps = finalSteps;
        this.ownPlayer = ownPlayer;
    }

    public <T extends GameState<T>> void fillCache(KnownReactionsPath<T> p, GameStateTreeNode<T> initialState) {
        OptionAnalysis<T> result = this.getPath(initialState, 0);
        for(Map.Entry<T,T> entry: result.getData().entrySet()) {
            p.cache(entry.getKey(), entry.getValue());
        }
    }

    private <T extends GameState<T>> OptionAnalysis<T> getPath(GameStateTreeNode<T> initialState, int steps) {
        if (initialState.getState().getGameSituation().isFinal()) {
            OptionAnalysis<T> result = new OptionAnalysis<>(initialState.getState());
            GameStateTreeNode<T> state = initialState.getParent();
            for(int i=0; i < finalSteps && state != null; i++) {
                result.data.put(state.getState(), initialState.getState());
            }
            return result;
        } else {
                initialState.expand();
                Set<OptionAnalysis<T>> options = initialState.getChildren().stream()
                        .map(s -> getPath(s, steps +1))
                        .collect(Collectors.toSet());
                OptionAnalysis<T> chosen = options.stream().max((s1, s2) -> (int) Math.signum(
                        s1.getExpectedResult().getGameSituation().getPlayerScore(ownPlayer)
                                - s2.getExpectedResult().getGameSituation().getPlayerScore(ownPlayer)
                        )).get();

                if(! initialState.getState().getNextChoice().equals(ownPlayer)) {
                    options.stream()
                            .filter(((Predicate<OptionAnalysis<T>>)chosen::equals).negate())
                            .map(OptionAnalysis::getData)
                            .forEach(chosen.getData()::putAll);
                }

                if(steps < initialSteps) {
                    chosen.data.put(initialState.getState(), chosen.getExpectedResult());
                }
                return chosen;
        }
    }

    private static class OptionAnalysis<T extends GameState<T>> {
        private final T expectedResult;
        private final Map<T,T> data = new HashMap<>();

        public OptionAnalysis(T expectedResult) {
            this.expectedResult = expectedResult;
        }
        public Map<T,T> getData() {
            return data;
        }
        public T getExpectedResult() {
            return expectedResult;
        }
    }


}

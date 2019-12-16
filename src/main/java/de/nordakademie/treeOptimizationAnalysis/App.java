package de.nordakademie.treeOptimizationAnalysis;

import de.nordakademie.treeOptimizationAnalysis.exitConditions.ExitCondition;
import de.nordakademie.treeOptimizationAnalysis.exitConditions.TurnCountingCondition;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.gameStates.InARowGameState;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.InARowGameHeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.UncompressedKnownReactionsPath;
import de.nordakademie.treeOptimizationAnalysis.traversalIterator.DepthFirstTreeTraversalIterator;
import de.nordakademie.treeOptimizationAnalysis.traversalIterator.TreeTraversalIterator;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class App implements Runnable {
    private List<ExitCondition.Factory> exitConditions;
    private List<Supplier<TreeTraversalIterator<?>>> treeTraversalIterators;
    private List<Game<?>> games;
    private List<KnownReactionsPath.Factory> caches;

    public static void main(String[] args) {
        InARowGameState state = new InARowGameState(6, 6, 4, true, Player.PLAYER_1);
        GameStateTreeNode<InARowGameState> node = new GameStateTreeNode<>(state, null, 0);

        InARowGameHeuristicEvaluation.PointsCounter pointsFinder = InARowGameHeuristicEvaluation.LENGTH_BY_CONSECUTIVE.accumulateBy(InARowGameHeuristicEvaluation.ACCUMULATE_BY_SQUARE_SUM);
        var controller1 = new Controller<>(new TurnCountingCondition<>(8), new DepthFirstTreeTraversalIterator<>(), InARowGameHeuristicEvaluation.factory(pointsFinder, 270).create(), new UncompressedKnownReactionsPath<>());
        var controller2 = new Controller<>(new TurnCountingCondition<>(3), new DepthFirstTreeTraversalIterator<>(), InARowGameHeuristicEvaluation.factory(pointsFinder, 270).create(), new UncompressedKnownReactionsPath<>());
        while (!node.getState().getGameSituation().isFinal()) {
            var controller = node.getState().getNextChoice() == Player.PLAYER_1 ? controller1 : controller2;
            node = controller.nextMove(node);
            System.out.println(node.getState() + "==================\n");
        }
    }

    @Override
    public void run() {
        games.forEach(this::analyze);
    }

    private <T extends GameState<T>> void analyze(Game<T> game) {
        for (T initialState : game.initialGameStates) {
            forEachController(game, Player.PLAYER_1, controller1 ->
                    forEachController(game, Player.PLAYER_2, controller2 -> {
                        GameStateTreeNode<T> node = new GameStateTreeNode<>(initialState, null, 0);

                        while (!node.getState().getGameSituation().isFinal()) {
                            var controller = node.getState().getNextChoice() == Player.PLAYER_1 ? controller1 : controller2;
                            node = controller.nextMove(node);
                            printMetrics(controller1, controller2, node);
                        }
                    }));
        }
    }

    private <T extends GameState<T>> void forEachController(Game<T> game, Player player, Consumer<Controller<T>> callback) {
        for (ExitCondition.Factory exitConditionFactory : exitConditions) {
            for (Supplier<TreeTraversalIterator<?>> iteratorFactory : treeTraversalIterators) {
                for (KnownReactionsPath.Factory cacheFactory : caches) {
                    for (HeuristicEvaluation.Factory<T> heuristicEvaluationFactory : game.heuristicEvaluations) {
                        callback.accept(this.<T>createController(exitConditionFactory, iteratorFactory, cacheFactory, heuristicEvaluationFactory, player));
                    }
                }
            }
        }
    }

    private <T extends GameState<T>> Controller<T> createController(
            ExitCondition.Factory exitConditionFactory,
            Supplier<TreeTraversalIterator<?>> treeTraversalIteratorFactory,
            KnownReactionsPath.Factory cacheFactory,
            HeuristicEvaluation.Factory<T> heuristicEvaluationFacotry,
            Player player) {
        var treeTraversalIterator = (TreeTraversalIterator<T>) treeTraversalIteratorFactory.get();
        var cache = cacheFactory.<T>create();
        var heuristicEvaluation = heuristicEvaluationFacotry.create();

        var exitCondition = exitConditionFactory.create(heuristicEvaluation, cache, player);
        return new Controller<>(exitCondition, treeTraversalIterator, heuristicEvaluation, cache);
    }

    private <T extends GameState<T>> void printMetrics(Controller<T> controller1, Controller<T> controller2, GameStateTreeNode<T> finalNode) {
        // TODO Implement
    }

    private static class Game<T extends GameState<T>> {
        private List<HeuristicEvaluation.Factory<T>> heuristicEvaluations;
        private List<T> initialGameStates;
    }
}

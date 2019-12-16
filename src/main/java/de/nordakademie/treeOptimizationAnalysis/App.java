package de.nordakademie.treeOptimizationAnalysis;

import de.nordakademie.treeOptimizationAnalysis.exitConditions.ExitCondition;
import de.nordakademie.treeOptimizationAnalysis.exitConditions.NeverExitCondition;
import de.nordakademie.treeOptimizationAnalysis.exitConditions.TurnCountingCondition;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.gameStates.InARowGameState;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.HeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.InARowGameHeuristicEvaluation;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.InARowGameHeuristicEvaluation.PointsCounter.Builder;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.KnownReactionsPath;
import de.nordakademie.treeOptimizationAnalysis.knownReactionPaths.UncompressedKnownReactionsPath;
import de.nordakademie.treeOptimizationAnalysis.traversalIterator.BreadthFirstTreeTraversalIterator;
import de.nordakademie.treeOptimizationAnalysis.traversalIterator.DepthFirstTreeTraversalIterator;
import de.nordakademie.treeOptimizationAnalysis.traversalIterator.TreeTraversalIterator;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.InARowGameHeuristicEvaluation.*;

public class App implements Runnable {
    // define Output
    private static final String ENTRY_SEPERATOR = "\n";
    private static final String FIELD_SEPERATOR = ", ";

    // settings
    private static final PrintStream out = System.out;
    private static final long TIMEOUT_IN_NANOS = 15l * 60l * 1000000000l;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    // concurrnecy
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private List<ExitCondition.Factory> exitConditions;
    private List<Supplier<TreeTraversalIterator<?>>> treeTraversalIterators;
    private List<Game<?>> games;
    private List<KnownReactionsPath.Factory> caches;



    public static void main(String[] args) {
        List<ExitCondition.Factory> exits = Arrays.asList(TurnCountingCondition.factory(2));
        List<Supplier<TreeTraversalIterator<?>>> iterators =
                Arrays.asList(() -> new BreadthFirstTreeTraversalIterator<>());
        List<KnownReactionsPath.Factory> caches = Arrays.asList(UncompressedKnownReactionsPath.FACTORY);

        List games = Arrays.asList(getInARowGame());
        new App(exits, iterators, games, caches).run();
    }

    private static Game getInARowGame() {
        InARowGameState ticTacToe = new InARowGameState(3, 3, 3, false, Player.PLAYER_1);
        InARowGameState fourWins = new InARowGameState(6, 4, 4, true, Player.PLAYER_1);
        List<InARowGameState> games = Arrays.asList(ticTacToe, fourWins);

        HeuristicEvaluation.Factory<InARowGameState> objectsInFrameSquareSum = InARowGameHeuristicEvaluation.factory(LENGTH_BY_OBJECTS_IN_POSSIBLE_BOX.accumulateBy(ACCUMULATE_BY_SQUARE_SUM), 0);

        List<HeuristicEvaluation.Factory> heuristics = new ArrayList<>();
        for(Builder pcb: new Builder[] {LENGTH_BY_OBJECTS_IN_POSSIBLE_BOX, LENGTH_BY_CONSECUTIVE}) {
            for(BinaryOperator<Integer> acc: new BinaryOperator[]{ACCUMULATE_BY_MAX, ACCUMULATE_BY_SQUARE_SUM}) {
                heuristics.add(factory(pcb.accumulateBy(acc),1000));
            }
        }
        return new Game(heuristics, games);
    }

    public App(List<ExitCondition.Factory> exitConditions, List<Supplier<TreeTraversalIterator<?>>> treeTraversalIterators, List<Game<?>> games, List<KnownReactionsPath.Factory> caches) {
        this.exitConditions = exitConditions;
        this.treeTraversalIterators = treeTraversalIterators;
        this.games = games;
        this.caches = caches;
    }

    @Override
    public void run() {
        printHeaders();
        games.forEach(this::analyze);
    }

    private <T extends GameState<T>> void analyze(Game<T> game) {
        for (T initialState : game.initialGameStates) {
            forEachController(game, Player.PLAYER_1, controller1Fact ->
                    forEachController(game, Player.PLAYER_2, controller2Fact -> {
                        executor.execute(() -> {
                            Controller<T> controller1 = controller1Fact.get();
                            Controller<T> controller2 = controller2Fact.get();
                            long start = time();
                            GameStateTreeNode<T> node = new GameStateTreeNode<>(initialState, null, 0);

                            while (!node.getState().getGameSituation().isFinal()) {
                                try {
                                    Controller<T> controller = node.getState().getNextChoice() == Player.PLAYER_1 ? controller1 : controller2;
                                    node = controller.nextMove(node);
                                } catch (Exception e) {
                                    printMetrics(controller1, controller2, node, initialState, time() - start, e.getClass().getSimpleName() + " during execution: " + e.getLocalizedMessage() + " at " + Arrays.toString(e.getStackTrace()));
                                    return;
                                }
                                if (time() - start > TIMEOUT_IN_NANOS) {
                                    printMetrics(controller1, controller2, node, initialState, time() - start, " timed out ");
                                    return;
                                }
                            }
                            printMetrics(controller1, controller2, node, initialState, time() - start, null);
                        });
                    }));
        }
    }

    private long time() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadUserTime();
    }

    private <T extends GameState<T>> void forEachController(Game<T> game, Player player, Consumer<Supplier<Controller<T>>> callback) {
        for (ExitCondition.Factory exitConditionFactory : exitConditions) {
            for (Supplier<TreeTraversalIterator<?>> iteratorFactory : treeTraversalIterators) {
                for (KnownReactionsPath.Factory cacheFactory : caches) {
                    for (HeuristicEvaluation.Factory<T> heuristicEvaluationFactory : game.heuristicEvaluations) {
                        callback.accept(() -> this.<T>createController(exitConditionFactory, iteratorFactory, cacheFactory, heuristicEvaluationFactory, player));
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
        TreeTraversalIterator<T> treeTraversalIterator = (TreeTraversalIterator<T>) treeTraversalIteratorFactory.get();
        KnownReactionsPath<T> cache = cacheFactory.<T>create();
        HeuristicEvaluation<T> heuristicEvaluation = heuristicEvaluationFacotry.create();

        ExitCondition<T> exitCondition = exitConditionFactory.create(heuristicEvaluation, cache, player);
        return new Controller<>(exitCondition, treeTraversalIterator, heuristicEvaluation, cache);
    }

    private void printHeaders() {
        for(NamedMetric m: NamedMetric.allMetrics) {
            out.print(m);
            out.print(FIELD_SEPERATOR);
        }
        out.print(ENTRY_SEPERATOR);
    };
    private void printMetrics(Controller<?> controller1, Controller<?> controller2, GameStateTreeNode<?> finalNode, GameState<?> initialNode, long time, String error) {
        synchronized (out) {
            for (NamedMetric m : NamedMetric.allMetrics) {
                Object val = m.messure(controller1, controller2, finalNode, initialNode, time, error);
                out.print(val == null ? " - " : val.toString());
                out.print(FIELD_SEPERATOR);
            }
            out.print(ENTRY_SEPERATOR);
        }
    }

    private static class Game<T extends GameState<T>> {
        private List<HeuristicEvaluation.Factory<T>> heuristicEvaluations;
        private List<T> initialGameStates;

        public Game(List<HeuristicEvaluation.Factory<T>> heuristicEvaluations, List<T> initialGameStates) {
            this.heuristicEvaluations = heuristicEvaluations;
            this.initialGameStates = initialGameStates;
        }
    }
}

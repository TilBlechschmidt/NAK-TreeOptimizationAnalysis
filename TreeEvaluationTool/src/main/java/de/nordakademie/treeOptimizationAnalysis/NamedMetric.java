package de.nordakademie.treeOptimizationAnalysis;

import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.gameStates.InARowGameState;
import de.nordakademie.treeOptimizationAnalysis.games.ChessGameState;
import de.nordakademie.treeOptimizationAnalysis.heuristicEvaluations.ChessHeuristicEvaluation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class NamedMetric {
    private static int counter = 0;


    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

    public static final List<NamedMetric> allMetrics = new ArrayList<>(Arrays.asList(
            new NamedMetric("id", (c1, c2, r, i, d, e) -> counter++),
            new NamedMetric("system time", (c1, c2, r, i, d, e) -> sdf.format(new Date())),
            new NamedMetric("board height",(c1,c2,r,i,d,e) -> r.getState().getBoardHeight()),
            new NamedMetric("board width",(c1,c2,r,i,d,e) -> r.getState().getBoardWidth()),
            new NamedMetric("game type", (c1,c2,r,i,d,e) -> r.getState().getClass().getSimpleName()),
            new NamedMetric("win length", (c1, c2, r,i,d,e) ->
                    (r.getState() instanceof InARowGameState) ?
                            ((InARowGameState)r.getState()).getWinLength()
                            : null),
            new NamedMetric("gravity",(c1, c2, r,i,d,e) ->
                    (r.getState() instanceof InARowGameState) ?
                            "" + ((InARowGameState)r.getState()).isGravity()
                            : null),
            new NamedMetric("turns played", (c1,c2,r,i,d,e) -> r.getDepth()),
            new NamedMetric("initial board", (c1,c2,r,i,d,e) -> i),
            new NamedMetric("final board", (c1,c2,r,i,d,e) -> r.getState()),
            new NamedMetric("result", (c1,c2,r,i,d,e) -> r.getState().getGameSituation()),
            new NamedMetric("errors", (c1,c2,r,i,d,e) -> e),
            new NamedMetric("full duration", (c1,c2,r,i,d,e) -> d),
/*            new NamedMetric("chronic", (c1,c2,r,i,d,e) -> {
                StringBuilder result = new StringBuilder();
                while (r != null) {
                    result.insert(0, r.getState());
                    result.insert(0," -> ");
                    r = r.getParent();
                }
                return result;
            }),*/
            new NamedMetric("pointsP1", (c1,c2,r,i,d,e) -> (r.getState() instanceof ChessGameState) ? new ChessHeuristicEvaluation().eval((ChessGameState) r.getState()).getPlayer1Score() : null)

    ));

    static {
        allMetrics.addAll(playerMetrics(Player.PLAYER_1));
        allMetrics.addAll(playerMetrics(Player.PLAYER_2));
    }

    private static NamedMetric playerMetric (Player player,String name, Function<Controller<?>, Object> metric) {
        if(Player.PLAYER_1.equals(player)) {
            return new NamedMetric(name + " of Player 1", (c1,c2,r,i,d,e) -> metric.apply(c1));
        } else if (Player.PLAYER_2.equals(player)) {
            return new NamedMetric(name + " of Player 2", (c1,c2,r,i,d,e) -> metric.apply(c2));
        } else {
            throw new IllegalArgumentException("can`t make a Metric of an unknown Player.");
        }

    }

    private static List<NamedMetric> playerMetrics(Player p) {
        return Arrays.asList(
                playerMetric(p, "expansion strategy", c -> c.getIterator().getClass().getSimpleName()),
                playerMetric(p, "cache", c -> c.getCache().getClass().getSimpleName()),
                playerMetric(p, "watched nodes", c -> c.getCache().size()),
                playerMetric(p, "evaluation strategy", Controller::getEvaluation),
                playerMetric(p, "exit condition type", c -> c.getExitCondition().getClass().getSimpleName()),
                playerMetric(p, "exit condition", Controller::getExitCondition),
                playerMetric(p, "total Speed", Controller::getTime)
        );
    }
















    private final String name;
    private final Metric metric;

    public NamedMetric(String name, Metric metric) {
        this.name = name;
        this.metric = metric;
    }

    private interface Metric  {
        Object measure(Controller<?> controller1, Controller<?> controller2, GameStateTreeNode<?> finalNode, GameState<?> initialNode, long fullTime, String error);
    }

    public Object measure(Controller<?> controller1, Controller<?> controller2, GameStateTreeNode<?> finalNode, GameState<?> initialNode, long fullTime, String error) {
        return metric.measure(controller1,controller2,finalNode,initialNode,fullTime, error);
    }

    @Override
    public String toString() {
        return name;
    }
}

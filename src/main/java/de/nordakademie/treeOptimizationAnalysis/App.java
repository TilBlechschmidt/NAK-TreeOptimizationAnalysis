package de.nordakademie.treeOptimizationAnalysis;

import de.nordakademie.treeOptimizationAnalysis.games.InARowGameState;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

public class App {
    private static void traverseDepthFirst(GameState state) {
        System.out.println(state.toString());

        Set<GameState> nextStates = state.getNextStates();
        for (GameState nextState : nextStates) {
            traverseDepthFirst(nextState);
        }
    }

    private static void traverseBreadthFirst(GameState state) {
        Queue<GameState> queue = new ArrayDeque<>();
        queue.add(state);

        while (!queue.isEmpty()) {
            GameState dequeued = queue.poll();
            System.out.println(dequeued.toString());

            Set<GameState> nextStates = dequeued.getNextStates();
            queue.addAll(nextStates);
        }
    }

    public static void main(String[] args) {
        System.out.println(-0.0 < 0.0);
        InARowGameState state = new InARowGameState(2, 2, 2, true, Player.PLAYER_1);

        traverseBreadthFirst(state);
//        traverseDepthFirst(state);
    }
}

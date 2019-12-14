package de.nordakademie.treeOptimizationAnalysis.games;

import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static de.nordakademie.treeOptimizationAnalysis.Player.PLAYER_1;
import static de.nordakademie.treeOptimizationAnalysis.Player.PLAYER_2;

public class InARowGameState implements GameState {
    private final int emptyField = 0;

    private final int width, height;

    private final int[][] field;
    private final Player currentPlayer;
    private final int winLength;
    private final boolean gravity;

    public InARowGameState(int width, int height, int winLength, boolean gravity, Player currentPlayer) {
        this(width, height, winLength, gravity, currentPlayer, new int[width][height]);
    }

    private InARowGameState(int width, int height, int winLength, boolean gravity, Player currentPlayer, int[][] field) {
        this.width = width;
        this.height = height;
        this.field = field;
        this.winLength = winLength;
        this.gravity = gravity;
        this.currentPlayer = currentPlayer;
    }

    private InARowGameState createChildState(Consumer<int[][]> fieldMutator) {
        // 1. Create a copy of the field
        int[][] newField = new int[width][height];

        for (int i = 0; i < width; i++) {
            System.arraycopy(field[i], 0, newField[i], 0, height);
        }

        // 2. Mutate the field
        fieldMutator.accept(newField);

        // 3. Create a cloned instance
        return new InARowGameState(width, height, winLength, gravity, getNextChoice(), newField);
    }

    @Override
    public Player getNextChoice() {
        return currentPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    @Override
    public Set<GameState> getNextStates() {
        Set<GameState> newStates = new HashSet<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (field[x][y] == emptyField) {
                    int fX = x, fY = y;
                    newStates.add(createChildState(field -> field[fX][fY] = currentPlayer.getIndex()));

                    if (gravity) {
                        break;
                    }
                }
            }
        }

        return newStates;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public Map<Player, Double> eval() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (field[x][y] == emptyField) {
                    stringBuilder.append('-');
                } else {
                    stringBuilder.append(field[x][y]);
                }
                stringBuilder.append(' ');
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
}

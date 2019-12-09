package de.nordakademie.treeOptimizationAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InARowGameState implements GameState {
    private final int emptyField = 0;

    private final int width, height;

    private final int[][] field;
    private final Player currentPlayer = Player.PLAYER_1;
    private final int winLength;
    private final boolean gravity;

    public InARowGameState(int width, int height, int winLength, boolean gravity) {
        this(width, height, winLength, gravity, new int[width][height]);
    }

    private InARowGameState(int width, int height, int winLength, boolean gravity, int[][] field) {
        this.width = width;
        this.height = height;
        this.field = field;
        this.winLength = winLength;
        this.gravity = gravity;
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
        return new InARowGameState(width, height, winLength, gravity, newField);
    }

    @Override
    public List<GameState> getNextStates() {
        List<GameState> newStates = new ArrayList<>();

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
    public GameState getParent() {
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

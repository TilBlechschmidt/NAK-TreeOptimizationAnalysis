package de.nordakademie.treeOptimizationAnalysis.games;

import de.nordakademie.treeOptimizationAnalysis.GameState;
import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.PlayerSituation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

import static de.nordakademie.treeOptimizationAnalysis.Player.PLAYER_1;
import static de.nordakademie.treeOptimizationAnalysis.Player.PLAYER_2;

public class InARowGameState implements GameState {
    private final int width, height;

    private final Player[][] field;
    private final Player currentPlayer;
    private final int winLength;
    private final boolean gravity;

    public InARowGameState(int width, int height, int winLength, boolean gravity, Player currentPlayer) {
        this(width, height, winLength, gravity, currentPlayer, new Player[width][height]);
    }

    private InARowGameState(int width, int height, int winLength, boolean gravity, Player currentPlayer, Player[][] field) {
        this.width = width;
        this.height = height;
        this.field = field;
        this.winLength = winLength;
        this.gravity = gravity;
        this.currentPlayer = currentPlayer;
    }

    public Player[][] getField() {
        return field;
    }

    public int getWinLength() {
        return winLength;
    }

    private InARowGameState createChildState(Consumer<Player[][]> fieldMutator) {
        // 1. Create a copy of the field
        Player[][] newField = new Player[width][height];

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
                if (field[x][y] == null) {
                    int fX = x, fY = y;
                    newStates.add(createChildState(field -> field[fX][fY] = currentPlayer));

                    if (gravity) {
                        break;
                    }
                }
            }
        }

        return newStates;
    }

    @Override
    public Map<Player, PlayerSituation> getGameSituation() {
        Map<Player, PlayerSituation> situationMap = new HashMap<>();
        var ref = new Object() {
            int currentLength = 0;
            Player previousField = null;
        };

        BinaryOperator<Integer> checkField = (x, y) -> {
            // TODO Replace this with proper loops
            if (x >= width || y >= height) return null;
            Player currentField = field[x][y];

            if (ref.previousField != currentField) ref.currentLength = 0;
            if (currentField != null) ref.currentLength++;
            if (ref.currentLength >= winLength) {
                situationMap.put(currentField, PlayerSituation.HasWon);
            }

            ref.previousField = currentField;

            return null;
        };

        // Check for vertical lines
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                checkField.apply(x, y);
            }
            ref.currentLength = 0;
            ref.previousField = null;
        }

        // Check for horizontal lines
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                checkField.apply(x, y);
            }
            ref.currentLength = 0;
            ref.previousField = null;
        }

        // Check for diagonal lines
        int maximumXY = Math.min(width, height);
        for (int startX = 0; startX < maximumXY; startX++) {
            // Bottom left to top right
            for (int xy = 0; xy < maximumXY; xy++) {
                checkField.apply(xy + startX, xy);
            }
            ref.currentLength = 0;
            ref.previousField = null;

            // Top left to bottom right
            for (int xy = 0; xy < maximumXY; xy++) {
                checkField.apply(xy + startX, (maximumXY - 1) - xy);
            }
            ref.currentLength = 0;
            ref.previousField = null;
        }

        // Detect Tie conditions
        boolean foundValidMove = false;
        outer: for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (field[x][y] == null) {
                    foundValidMove = true;
                    break outer;
                }
            }
        }
        if(!foundValidMove) {
            situationMap.put(PLAYER_1, PlayerSituation.Tie);
            situationMap.put(PLAYER_2, PlayerSituation.Tie);
            return situationMap;
        }

        return situationMap;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (field[x][y] == null) {
                    stringBuilder.append('-');
                } else {
                    stringBuilder.append(field[x][y].getIndex());
                }
                stringBuilder.append(' ');
            }
            stringBuilder.append('\n');
        }

        Map<Player,
 PlayerSituation> situation = getGameSituation();
        situation.forEach(((player, playerSituation) -> {
            stringBuilder.append("P");
            stringBuilder.append(player.getIndex());
            stringBuilder.append(": ");
            stringBuilder.append(playerSituation);
            stringBuilder.append("\n");
        }));

        return stringBuilder.toString();
    }
}

package de.nordakademie.treeOptimizationAnalysis.games;

import de.nordakademie.treeOptimizationAnalysis.Player;

import java.util.*;

/**
 * Represents a Chess Piece,
 * It is Immutable
 */
public class ChessPiece {
    private static class Dir {
        final int x;
        final int y;
        Dir(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final Dir[] ROOK_DIRS = {new Dir(1,0),new Dir(0,1),new Dir(-1,0),new Dir(0,-1)};
    private static final Dir[] BISHOP_DIRS = {new Dir(1,1),new Dir(1,-1),new Dir(-1,1),new Dir(-1,-1)};
    private static final Dir[] KNIGHT_DIRS = {new Dir(2,1), new Dir(1,2), new Dir(-1, 2),
            new Dir(-2,1), new Dir(-2,-1), new Dir(-1,-2), new Dir(1, -2), new Dir(2,-1)};
    private static final Dir[] QUEEN_AND_KING_DIRS = {new Dir(1,0), new Dir(1,1), new Dir(0,1),
            new Dir(-1,1), new Dir(-1,0), new Dir(-1,-1), new Dir(0,-1), new Dir(1,-1)};

    public static ChessPiece rook(Player player) {return new ChessPiece(player, 5, true, ROOK_DIRS,false, "r"); }
    public static ChessPiece bishop(Player player) {return new ChessPiece(player, 3, true, BISHOP_DIRS, false,"b"); }
    public static ChessPiece knight(Player player) {return new ChessPiece(player, 3, false, KNIGHT_DIRS, false, "n"); }
    public static ChessPiece queen(Player player) {return new ChessPiece(player, 5, true, QUEEN_AND_KING_DIRS, false, "q"); }
    public static ChessPiece king(Player player) {return new ChessPiece(player, 5, false, QUEEN_AND_KING_DIRS, true, "k"); }
    public static ChessPiece pawn(Player player) { return pawn(player, false);}

    public static ChessPiece pawn(Player player, boolean moved) {
        int dy = player.equals(Player.PLAYER_1) ? 1:-1;
        return new ChessPiece(player, 1, false, null, false, "p") {
            @Override
            public List<ChessGameState.Field> possibleGoals(ChessGameState.Field ownPosition, ChessPiece[][] board) {
                List<ChessGameState.Field> result = new ArrayList<>();
                ChessGameState.Field step = new ChessGameState.Field(ownPosition.getX(), ownPosition.getY() + dy);
                if(step.isIn(board) && step.get(board) == null) {
                    result.add(step);
                    if(! moved) {
                        ChessGameState.Field doubleStep = new ChessGameState.Field(ownPosition.getX(), ownPosition.getY() + 2*dy);
                        if(doubleStep.isIn(board) && doubleStep.get(board) == null) {
                            result.add(doubleStep);
                        }
                    }
                }

                for(int dx: new int[]{-1,1}) {
                    ChessGameState.Field beat = new ChessGameState.Field(ownPosition.getX()+dx, ownPosition.getY() + dy);
                    if(beat.isIn(board) && beat.get(board) == null) {
                        ChessPiece beaten = beat.get(board);
                        if(beaten != null && !beaten.getPlayer().equals(player)) {
                            result.add(beat);
                        }
                    }
                }
                return result;
            }

            @Override
            public Set<ChessPiece> onMove(ChessGameState.Field field, ChessPiece[][] board) {
                if(new ChessGameState.Field(field.getX(), field.getY() + dy).isIn(board)) {
                    return Collections.singleton(pawn(player, true));
                } else {
                    return new HashSet<>(Arrays.asList(rook(player),bishop(player),knight(player), queen(player)));
                }
            }
        };
    }

    private final Player player;
    private final int value;
    private final String code;
    private final boolean multiSteps;
    private final boolean isKing;
    private final Dir[] directions;

    public ChessPiece(Player player, int value, boolean multiSteps, Dir[] directions, boolean isKing, String code) {
        this.player = player;
        this.value = value;
        this.multiSteps = multiSteps;
        this.directions = directions;
        this.isKing = isKing;
        this.code = (Player.PLAYER_1.equals(player) ? code : code.toUpperCase());
    }

    public List<ChessGameState.Field> possibleGoals(ChessGameState.Field ownPosition, ChessPiece[][] board) {
        List<ChessGameState.Field> result = new ArrayList<>();
        for(Dir dir: directions) {
            int count = 1;
            while (true) {
                ChessGameState.Field field = field(ownPosition, dir, count);
                if(field.isIn(board)) {
                    ChessPiece piece = field.get(board);
                    if (piece == null) {
                        result.add(field);
                        if(! multiSteps) {
                            break;
                        }
                    } else if(!piece.getPlayer().equals(player)){
                        result.add(field);
                        break;
                    } else break;
                    count++;
                } else {
                    break;
                }
            }

        }
//        System.out.println(result);
        return result;
    }

    private ChessGameState.Field field(ChessGameState.Field ownPosition, Dir dir, int count) {
        return new ChessGameState.Field(ownPosition.getX() + dir.x * count, ownPosition.getY() + dir.y * count);
    }

    public int getValue() {
        return value;
    }

    public Player getPlayer() {
        return player;
    }
    public Set<ChessPiece> onMove(ChessGameState.Field destination, ChessPiece[][] board) {
        return Collections.singleton(this);
    }

    public boolean isKing()  {
        return isKing;
    }

    public String toString() {
        return code;
    }
}

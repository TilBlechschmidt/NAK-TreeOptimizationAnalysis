package de.nordakademie.treeOptimizationAnalysis.exitConditions;

import de.nordakademie.treeOptimizationAnalysis.ExitConditionTest;
import de.nordakademie.treeOptimizationAnalysis.Player;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import de.nordakademie.treeOptimizationAnalysis.gameStates.InARowGameState;
import de.nordakademie.treeOptimizationAnalysis.games.ChessGameState;
import de.nordakademie.treeOptimizationAnalysis.games.ChessPiece;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.stream.Stream;

public class OrExitConditionTest<T extends GameState<T>> extends ExitConditionTest<T> {
    @Override
    protected Stream<ExitCondition<T>> exitConditionsUnderTest() {
        return Stream.of(new AndExitCondition<T>(new NeverExitCondition<>()));
    }

    @Override
    protected Stream<GameStateTreeNode<T>> sampleStates() {
        GameState chess = new ChessGameState(new ChessPiece[][]{{ChessPiece.king(Player.PLAYER_1), null, null, ChessPiece.king(Player.PLAYER_2)}}, Player.PLAYER_1);
        GameState chessFinal = new ChessGameState(new ChessPiece[][]{{ChessPiece.king(Player.PLAYER_1), null, ChessPiece.king(Player.PLAYER_2)}}, Player.PLAYER_1);
        GameState inARow = new InARowGameState(3,3,3,false, Player.PLAYER_1);
        return Stream.of(chess,chessFinal,inARow).map(s -> new GameStateTreeNode<>(s, null, 0));
    }

    @Test
    public void orExitConditionShouldReturnFalseIfAllExitConditionReturnsFalse() {
        ExitCondition falseExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(falseExitCondition.shouldBreak(null,null)).thenReturn(false);

        ExitCondition or1 = new OrExitCondition<>(falseExitCondition);
        ExitCondition or2 = new OrExitCondition<>(falseExitCondition, falseExitCondition);
        ExitCondition or3 = new OrExitCondition<>(falseExitCondition, falseExitCondition, falseExitCondition);

        Assert.assertFalse(or1.shouldBreak(null,null));
        Assert.assertFalse(or2.shouldBreak(null,null));
        Assert.assertFalse(or3.shouldBreak(null,null));
    }

    @Test
    public void orExitConditionShouldReturnTrueIfSomeExitConditionReturnsTrue() {
        ExitCondition falseExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(falseExitCondition.shouldBreak(null,null)).thenReturn(false);
        ExitCondition trueExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(trueExitCondition.shouldBreak(null,null)).thenReturn(true);

        ExitCondition or1of2 = new OrExitCondition<>(trueExitCondition, falseExitCondition);
        ExitCondition or1of3 = new OrExitCondition<>(falseExitCondition, trueExitCondition, falseExitCondition);
        ExitCondition or2of3 = new OrExitCondition<>(trueExitCondition, falseExitCondition, trueExitCondition);

        Assert.assertTrue(or1of2.shouldBreak(null,null));
        Assert.assertTrue(or1of3.shouldBreak(null,null));
        Assert.assertTrue(or2of3.shouldBreak(null,null));
    }

    @Test
    public void orExitConditionShouldReturnTrueIfAllExitConditionReturnsTrue() {
        ExitCondition trueExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(trueExitCondition.shouldBreak(null,null)).thenReturn(true);

        ExitCondition or1 = new OrExitCondition<>(trueExitCondition);
        ExitCondition or2 = new OrExitCondition<>(trueExitCondition, trueExitCondition);
        ExitCondition or3 = new OrExitCondition<>(trueExitCondition, trueExitCondition, trueExitCondition);

        Assert.assertTrue(or1.shouldBreak(null,null));
        Assert.assertTrue(or2.shouldBreak(null,null));
        Assert.assertTrue(or3.shouldBreak(null,null));
    }

}
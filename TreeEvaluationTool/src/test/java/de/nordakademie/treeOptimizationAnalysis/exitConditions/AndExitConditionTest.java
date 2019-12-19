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

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.Assert.*;


public class AndExitConditionTest extends ExitConditionTest {
    @Override
    protected Stream<ExitCondition> exitConditionsUnderTest() {
        return Arrays.<ExitCondition>asList(new AndExitCondition(new NeverExitCondition())).stream();
    }

    @Override
    protected Stream<GameStateTreeNode> sampleStates() {
        GameState chess = new ChessGameState(new ChessPiece[][]{{ChessPiece.king(Player.PLAYER_1), null, null, ChessPiece.king(Player.PLAYER_2)}}, Player.PLAYER_1);
        GameState chessFinal = new ChessGameState(new ChessPiece[][]{{ChessPiece.king(Player.PLAYER_1), null, ChessPiece.king(Player.PLAYER_2)}}, Player.PLAYER_1);
        GameState inARow = new InARowGameState(3,3,3,false, Player.PLAYER_1);
        return Arrays.asList(chess,chessFinal,inARow).stream().map(s -> new GameStateTreeNode<>(s, null, 0));
    }

    @Test
    public void andExitConditionShouldReturnFalseIfAllExitConditionReturnsFalse() {
        ExitCondition falseExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(falseExitCondition.shouldBreak(null,null)).thenReturn(false);

        ExitCondition and1 = new AndExitCondition(falseExitCondition);
        ExitCondition and2 = new AndExitCondition(falseExitCondition, falseExitCondition);
        ExitCondition and3 = new AndExitCondition(falseExitCondition, falseExitCondition, falseExitCondition);

        Assert.assertFalse(and1.shouldBreak(null,null));
        Assert.assertFalse(and2.shouldBreak(null,null));
        Assert.assertFalse(and3.shouldBreak(null,null));
    }

    @Test
    public void andExitConditionShouldReturnFalseIfSomeExitConditionReturnsFalse() {
        ExitCondition falseExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(falseExitCondition.shouldBreak(null,null)).thenReturn(false);
        ExitCondition trueExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(trueExitCondition.shouldBreak(null,null)).thenReturn(true);

        ExitCondition and1of2 = new AndExitCondition(trueExitCondition, falseExitCondition);
        ExitCondition and1of3 = new AndExitCondition(falseExitCondition, trueExitCondition, falseExitCondition);
        ExitCondition and2of3 = new AndExitCondition(trueExitCondition, falseExitCondition, trueExitCondition);

        Assert.assertFalse(and1of2.shouldBreak(null,null));
        Assert.assertFalse(and1of3.shouldBreak(null,null));
        Assert.assertFalse(and2of3.shouldBreak(null,null));
    }

    @Test
    public void andExitConditionShouldReturnTrueIfAllExitConditionReturnsTrue() {
        ExitCondition trueExitCondition = Mockito.mock(ExitCondition.class);
        Mockito.when(trueExitCondition.shouldBreak(null,null)).thenReturn(true);

        ExitCondition and1 = new AndExitCondition(trueExitCondition);
        ExitCondition and2 = new AndExitCondition(trueExitCondition, trueExitCondition);
        ExitCondition and3 = new AndExitCondition(trueExitCondition, trueExitCondition, trueExitCondition);

        Assert.assertTrue(and1.shouldBreak(null,null));
        Assert.assertTrue(and2.shouldBreak(null,null));
        Assert.assertTrue(and3.shouldBreak(null,null));
    }

}
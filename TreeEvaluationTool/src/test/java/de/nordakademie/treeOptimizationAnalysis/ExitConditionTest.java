package de.nordakademie.treeOptimizationAnalysis;

import de.nordakademie.treeOptimizationAnalysis.exitConditions.ExitCondition;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameState;
import de.nordakademie.treeOptimizationAnalysis.gameStates.GameStateTreeNode;
import org.junit.Assert;
import org.junit.Test;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public abstract class ExitConditionTest<T extends GameState<T>> {

    protected abstract Stream<ExitCondition<T>> exitConditionsUnderTest();
    protected abstract Stream<GameStateTreeNode<T>> sampleStates();

    @Test
    public void exitConditionsShouldExitOnFinalState() {
        exitConditionsUnderTest().forEach(exitCondition ->
            sampleStates().forEach( sampleStart ->
                sampleStates().forEach( sampleEnd -> {
                    if(sampleEnd.getState().getGameSituation().isFinal()) {
                        Assert.assertTrue(exitCondition.shouldBreak(sampleStart,sampleEnd));
                    }
                })
            )
        );
    }

    @Test
    public void exitConditionsShouldBesStateless() {
        exitConditionsUnderTest().forEach(exitCondition ->
                sampleStates().forEach( sampleStart ->
                        sampleStates().forEach( sampleEnd -> {
                            if(sampleEnd.getState().getGameSituation().isFinal()) {
                                Assert.assertTrue(exitCondition.shouldBreak(sampleStart,sampleEnd));
                            }
                        })
                )
        );

    }
}
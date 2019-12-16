package de.nordakademie.treeOptimizationAnalysis;

import org.junit.Test;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public abstract class ExitConditionTest {

    protected abstract Stream<ExitCondition<?>> exitConditionsUnderTest();

    @Test
    public void exitConditionsShouldExitOnFinalState() {
        GameStateTreeNode evalBase = Mockito.mock
        exitConditionsUnderTest().forEach(exitCondition -> {
            exitCondition.shouldBreak()
        });
    }
}
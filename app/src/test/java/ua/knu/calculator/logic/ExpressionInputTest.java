package ua.knu.calculator.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class ExpressionInputTest {

    private final ExpressionInput input = new ExpressionInput();

    @Test
    void tokensAreJoinedIntoText() {
        input.add("sin(");
        input.add("3");
        input.add("0");
        input.add(")");

        assertEquals("sin(30)", input.getText());
    }

    @Test
    void removeLastDeletesWholeToken() {
        input.add("2");
        input.add("×");
        input.add("sin(");

        input.removeLast();

        assertEquals("2×", input.getText());
    }

    @Test
    void removeLastOnEmptyInputDoesNothing() {
        input.removeLast();

        assertTrue(input.isEmpty());
        assertEquals("", input.getText());
    }

    @Test
    void clearRemovesEverything() {
        input.add("1");
        input.add("+");

        input.clear();

        assertTrue(input.isEmpty());
    }

    @Test
    void replaceWithKeepsOnlyResult() {
        input.add("2");
        input.add("+");
        input.add("3");

        input.replaceWith("5");

        assertEquals("5", input.getText());
        input.removeLast();
        assertTrue(input.isEmpty());
    }

    @Test
    void tokensCanBeSavedAndRestored() {
        input.add("√(");
        input.add("9");
        List<String> saved = input.getTokens();

        ExpressionInput restored = new ExpressionInput();
        restored.setTokens(saved);

        assertFalse(restored.isEmpty());
        assertEquals(Arrays.asList("√(", "9"), restored.getTokens());
        assertEquals("√(9", restored.getText());
    }
}

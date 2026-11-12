package ua.knu.calculator.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ua.knu.calculator.expression.CalculatorException;

class ExpressionParserTest {

    private final ExpressionParser parser = new ExpressionParser();

    private double calculate(String text) {
        return parser.parse(text).evaluate();
    }

    @Test
    void parsesNumbers() {
        assertEquals(42, calculate("42"));
        assertEquals(3.5, calculate("3.5"));
        assertEquals(0.5, calculate(".5"));
    }

    @Test
    void addsAndSubtractsFromLeftToRight() {
        assertEquals(3, calculate("10−4−3"));
        assertEquals(8, calculate("2+3+3"));
    }

    @Test
    void multiplicationGoesBeforeAddition() {
        assertEquals(14, calculate("2+3×4"));
        assertEquals(3.5, calculate("1+5÷2"));
    }

    @Test
    void bracketsChangeOrder() {
        assertEquals(20, calculate("(2+3)×4"));
        assertEquals(2, calculate("((1+1))"));
    }

    @Test
    void lastBracketCanBeOpen() {
        assertEquals(20, calculate("4×(2+3"));
    }

    @Test
    void unaryMinus() {
        assertEquals(-3, calculate("−5+2"));
        assertEquals(-6, calculate("2×−3"));
        assertEquals(5, calculate("−−5"));
    }

    @Test
    void powerGoesFromRightToLeft() {
        assertEquals(1024, calculate("2^10"));
        assertEquals(512, calculate("2^3^2"));
        assertEquals(0.5, calculate("2^−1"));
        // спочатку степінь, потім мінус
        assertEquals(-4, calculate("−2^2"));
    }

    @Test
    void acceptsUsualMinus() {
        assertEquals(-1, calculate("-3+2"));
    }

    @Test
    void divisionByZeroThrowsException() {
        assertThrows(CalculatorException.class, () -> calculate("5÷0"));
        assertThrows(CalculatorException.class, () -> calculate("1÷(2−2)"));
    }

    @Test
    void wrongExpressionsThrowException() {
        String[] wrong = {"", "2+", "2++3", "×2", "(", "()", "(2+3))",
                "1.2.3", ".", "abc", "2 + 3"};
        for (String text : wrong) {
            assertThrows(CalculatorException.class, () -> calculate(text),
                    text);
        }
    }
}

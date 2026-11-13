package ua.knu.calculator.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ua.knu.calculator.expression.AngleUnit;
import ua.knu.calculator.expression.CalculatorException;

class ExpressionParserTest {

    private static final double DELTA = 1e-12;

    private final ExpressionParser parser =
            new ExpressionParser(AngleUnit.DEGREES);
    private final ExpressionParser radiansParser =
            new ExpressionParser(AngleUnit.RADIANS);

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

    @Test
    void trigonometricFunctionsUseDegrees() {
        assertEquals(0.5, calculate("sin(30)"), DELTA);
        assertEquals(0.5, calculate("cos(60)"), DELTA);
        assertEquals(1, calculate("tan(45)"), DELTA);
        assertEquals(0, calculate("sin(180)"));
    }

    @Test
    void trigonometricFunctionsUseRadians() {
        assertEquals(1, radiansParser.parse("sin(π÷2)").evaluate(), DELTA);
        assertEquals(-1, radiansParser.parse("cos(π)").evaluate(), DELTA);
    }

    @Test
    void logarithmsAndRoot() {
        assertEquals(1, calculate("ln(e)"), DELTA);
        assertEquals(3, calculate("log(1000)"), DELTA);
        assertEquals(4, calculate("√(16)"));
        assertEquals(5, calculate("√(9+16"));
    }

    @Test
    void constants() {
        assertEquals(Math.PI, calculate("π"));
        assertEquals(Math.E, calculate("e"));
        assertEquals(2 * Math.PI, calculate("2×π"), DELTA);
    }

    @Test
    void factorial() {
        assertEquals(120, calculate("5!"));
        assertEquals(1, calculate("0!"));
        assertEquals(6, calculate("(1+2)!"));
        assertEquals(720, calculate("3!!"));
    }

    @Test
    void factorialGoesBeforePowerAndMinus() {
        assertEquals(64, calculate("2^3!"));
        assertEquals(-6, calculate("−3!"));
    }

    @Test
    void parsesNumbersWithExponent() {
        assertEquals(1.5e20, calculate("1.5E+20"));
        assertEquals(2e-7, calculate("2E-7"));
        assertEquals(2e-7, calculate("2E−7"));
        assertEquals(3e5, calculate("3E5"));
    }

    @Test
    void wrongFunctionArgumentsThrowException() {
        String[] wrong = {"ln(0)", "log(−5)", "√(−4)", "tan(90)", "(−1)!",
                "2.5!", "171!"};
        for (String text : wrong) {
            assertThrows(CalculatorException.class, () -> calculate(text),
                    text);
        }
    }

    @Test
    void wrongFunctionsThrowException() {
        String[] wrong = {"sin", "sin30", "sin()", "!", "2E", "2E+",
                "E5", "cos(1)E"};
        for (String text : wrong) {
            assertThrows(CalculatorException.class, () -> calculate(text),
                    text);
        }
    }
}

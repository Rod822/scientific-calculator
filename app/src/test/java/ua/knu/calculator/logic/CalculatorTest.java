package ua.knu.calculator.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ua.knu.calculator.expression.AngleUnit;
import ua.knu.calculator.expression.CalculatorException;

class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void returnsFormattedText() {
        assertEquals("0.3", calculator.calculate("0.1+0.2"));
        assertEquals("14", calculator.calculate("2+3×4"));
        assertEquals("120", calculator.calculate("5!"));
        assertEquals("1E+20", calculator.calculate("10^20"));
    }

    @Test
    void degreesByDefault() {
        assertEquals(AngleUnit.DEGREES, calculator.getAngleUnit());
        assertEquals("0.5", calculator.calculate("sin(30)"));
        assertEquals("0", calculator.calculate("sin(180)"));
    }

    @Test
    void switchesToRadians() {
        calculator.setAngleUnit(AngleUnit.RADIANS);

        assertEquals("1", calculator.calculate("sin(π÷2)"));
        assertEquals("-1", calculator.calculate("cos(π)"));
    }

    @Test
    void resultCanBeUsedAgain() {
        assertEquals("1E+20", calculator.calculate("1E+20+1"));
        assertEquals("4E-7", calculator.calculate("2E-7×2"));
    }

    @Test
    void wrongResultsThrowException() {
        String[] wrong = {"5÷0", "ln(0)", "(−8)^0.5", "10^400", "2+"};
        for (String text : wrong) {
            assertThrows(CalculatorException.class,
                    () -> calculator.calculate(text), text);
        }
    }
}

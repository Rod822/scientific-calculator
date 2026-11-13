package ua.knu.calculator.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResultFormatterTest {

    private final ResultFormatter formatter = new ResultFormatter();

    @Test
    void removesFloatingPointError() {
        assertEquals("0.3", formatter.format(0.1 + 0.2));
    }

    @Test
    void keepsTwelveDigits() {
        assertEquals("0.333333333333", formatter.format(1.0 / 3));
        assertEquals("3.14159265359", formatter.format(Math.PI));
    }

    @Test
    void wholeNumbersWithoutPoint() {
        assertEquals("120", formatter.format(120));
        assertEquals("-7", formatter.format(-7));
        assertEquals("1000000", formatter.format(1e6));
        assertEquals("0", formatter.format(0));
        assertEquals("0", formatter.format(-0.0));
    }

    @Test
    void bigAndSmallNumbersUseExponent() {
        assertEquals("1E+20", formatter.format(1e20));
        assertEquals("1E+12", formatter.format(1e12));
        assertEquals("999999999999", formatter.format(999999999999.0));
        assertEquals("2E-7", formatter.format(2e-7));
        assertEquals("0.000001", formatter.format(1e-6));
    }
}

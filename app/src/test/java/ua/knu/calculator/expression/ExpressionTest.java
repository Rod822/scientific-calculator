package ua.knu.calculator.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ExpressionTest {

    @Test
    void numberReturnsItsValue() {
        assertEquals(2.5, new NumberExpression(2.5).evaluate());
    }

    @Test
    void negateChangesSign() {
        Expression expression = new NegateExpression(new NumberExpression(3));

        assertEquals(-3, expression.evaluate());
    }

    @Test
    void binaryExpressionUsesItsOperation() {
        Expression two = new NumberExpression(2);
        Expression five = new NumberExpression(5);

        assertEquals(7, new BinaryExpression(two, five, Operation.ADD)
                .evaluate());
        assertEquals(-3, new BinaryExpression(two, five, Operation.SUBTRACT)
                .evaluate());
        assertEquals(10, new BinaryExpression(two, five, Operation.MULTIPLY)
                .evaluate());
        assertEquals(0.4, new BinaryExpression(two, five, Operation.DIVIDE)
                .evaluate());
        assertEquals(32, new BinaryExpression(two, five, Operation.POWER)
                .evaluate());
    }

    @Test
    void divisionByZeroThrowsException() {
        Expression expression = new BinaryExpression(new NumberExpression(1),
                new NumberExpression(0), Operation.DIVIDE);

        assertThrows(CalculatorException.class, expression::evaluate);
    }
}

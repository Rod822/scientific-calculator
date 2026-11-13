package ua.knu.calculator.expression;

public class FactorialExpression implements Expression {

    // 171! вже не вміщується в double
    private static final int MAX_ARGUMENT = 170;

    private final Expression operand;

    public FactorialExpression(Expression operand) {
        this.operand = operand;
    }

    @Override
    public double evaluate() {
        double value = operand.evaluate();
        if (value < 0 || value > MAX_ARGUMENT || value != Math.floor(value)) {
            throw new CalculatorException("Factorial is defined only for "
                    + "whole numbers from 0 to " + MAX_ARGUMENT);
        }
        double result = 1;
        for (int i = 2; i <= value; i++) {
            result *= i;
        }
        return result;
    }
}

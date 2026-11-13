package ua.knu.calculator.logic;

import ua.knu.calculator.expression.AngleUnit;
import ua.knu.calculator.expression.CalculatorException;
import ua.knu.calculator.parser.ExpressionParser;

// фасад для екрана: отримує текст виразу, повертає готовий результат
public class Calculator {

    private final ResultFormatter formatter = new ResultFormatter();
    private AngleUnit angleUnit = AngleUnit.DEGREES;

    public AngleUnit getAngleUnit() {
        return angleUnit;
    }

    public void setAngleUnit(AngleUnit angleUnit) {
        this.angleUnit = angleUnit;
    }

    public String calculate(String text) {
        ExpressionParser parser = new ExpressionParser(angleUnit);
        double result = parser.parse(text).evaluate();
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new CalculatorException("Result is not a number");
        }
        return formatter.format(result);
    }
}

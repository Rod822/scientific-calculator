package ua.knu.calculator.expression;

public class FunctionExpression implements Expression {

    private final MathFunction function;
    private final Expression argument;
    private final AngleUnit angleUnit;

    public FunctionExpression(MathFunction function, Expression argument,
            AngleUnit angleUnit) {
        this.function = function;
        this.argument = argument;
        this.angleUnit = angleUnit;
    }

    @Override
    public double evaluate() {
        double value = argument.evaluate();
        // тригонометричні функції працюють з радіанами
        if (function.isTrigonometric()) {
            value = angleUnit.toRadians(value);
        }
        return function.apply(value);
    }
}

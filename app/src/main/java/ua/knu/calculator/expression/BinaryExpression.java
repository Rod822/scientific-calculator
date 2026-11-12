package ua.knu.calculator.expression;

public class BinaryExpression implements Expression {

    private final Expression left;
    private final Expression right;
    private final Operation operation;

    public BinaryExpression(Expression left, Expression right,
            Operation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public double evaluate() {
        return operation.apply(left.evaluate(), right.evaluate());
    }
}

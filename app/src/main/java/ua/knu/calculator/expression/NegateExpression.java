package ua.knu.calculator.expression;

// унарний мінус
public class NegateExpression implements Expression {

    private final Expression operand;

    public NegateExpression(Expression operand) {
        this.operand = operand;
    }

    @Override
    public double evaluate() {
        return -operand.evaluate();
    }
}

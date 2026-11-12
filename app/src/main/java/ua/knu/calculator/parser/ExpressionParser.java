package ua.knu.calculator.parser;

import ua.knu.calculator.expression.BinaryExpression;
import ua.knu.calculator.expression.CalculatorException;
import ua.knu.calculator.expression.Expression;
import ua.knu.calculator.expression.NegateExpression;
import ua.knu.calculator.expression.NumberExpression;
import ua.knu.calculator.expression.Operation;

// рекурсивний спуск: сума -> добуток -> унарний мінус -> степінь -> число
public class ExpressionParser {

    private String text;
    private int position;

    public Expression parse(String expressionText) {
        text = expressionText;
        position = 0;
        if (text.isEmpty()) {
            throw new CalculatorException("Expression is empty");
        }
        Expression expression = parseSum();
        if (position < text.length()) {
            throw unexpectedSymbol();
        }
        return expression;
    }

    private Expression parseSum() {
        Expression result = parseProduct();
        while (position < text.length()) {
            char symbol = text.charAt(position);
            if (symbol == '+') {
                position++;
                result = new BinaryExpression(result, parseProduct(),
                        Operation.ADD);
            } else if (symbol == '-' || symbol == '−') {
                position++;
                result = new BinaryExpression(result, parseProduct(),
                        Operation.SUBTRACT);
            } else {
                break;
            }
        }
        return result;
    }

    private Expression parseProduct() {
        Expression result = parseUnary();
        while (position < text.length()) {
            char symbol = text.charAt(position);
            if (symbol == '×') {
                position++;
                result = new BinaryExpression(result, parseUnary(),
                        Operation.MULTIPLY);
            } else if (symbol == '÷') {
                position++;
                result = new BinaryExpression(result, parseUnary(),
                        Operation.DIVIDE);
            } else {
                break;
            }
        }
        return result;
    }

    private Expression parseUnary() {
        if (position < text.length()) {
            char symbol = text.charAt(position);
            if (symbol == '-' || symbol == '−') {
                position++;
                return new NegateExpression(parseUnary());
            }
        }
        return parsePower();
    }

    // степінь рахується справа наліво: 2^3^2 = 2^9
    private Expression parsePower() {
        Expression base = parsePrimary();
        if (position < text.length() && text.charAt(position) == '^') {
            position++;
            return new BinaryExpression(base, parseUnary(), Operation.POWER);
        }
        return base;
    }

    private Expression parsePrimary() {
        if (position >= text.length()) {
            throw new CalculatorException("Unexpected end of expression");
        }
        char symbol = text.charAt(position);
        if (symbol == '(') {
            position++;
            Expression inner = parseSum();
            // дужку в кінці виразу можна не закривати, як у калькуляторі
            if (position < text.length()) {
                if (text.charAt(position) != ')') {
                    throw unexpectedSymbol();
                }
                position++;
            }
            return inner;
        }
        if (Character.isDigit(symbol) || symbol == '.') {
            return parseNumber();
        }
        throw unexpectedSymbol();
    }

    private Expression parseNumber() {
        int start = position;
        while (position < text.length()
                && (Character.isDigit(text.charAt(position))
                || text.charAt(position) == '.')) {
            position++;
        }
        String number = text.substring(start, position);
        try {
            return new NumberExpression(Double.parseDouble(number));
        } catch (NumberFormatException e) {
            throw new CalculatorException("Wrong number " + number, e);
        }
    }

    private CalculatorException unexpectedSymbol() {
        return new CalculatorException("Unexpected symbol '"
                + text.charAt(position) + "' at position " + position);
    }
}

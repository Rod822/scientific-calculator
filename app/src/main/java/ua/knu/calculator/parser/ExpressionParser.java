package ua.knu.calculator.parser;

import ua.knu.calculator.expression.AngleUnit;
import ua.knu.calculator.expression.BinaryExpression;
import ua.knu.calculator.expression.CalculatorException;
import ua.knu.calculator.expression.Expression;
import ua.knu.calculator.expression.FactorialExpression;
import ua.knu.calculator.expression.FunctionExpression;
import ua.knu.calculator.expression.MathFunction;
import ua.knu.calculator.expression.NegateExpression;
import ua.knu.calculator.expression.NumberExpression;
import ua.knu.calculator.expression.Operation;

// рекурсивний спуск: сума -> добуток -> унарний мінус -> степінь ->
// факторіал -> число, константа, функція або дужки
public class ExpressionParser {

    private final AngleUnit angleUnit;
    private String text;
    private int position;

    public ExpressionParser(AngleUnit angleUnit) {
        this.angleUnit = angleUnit;
    }

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
        Expression base = parseFactorial();
        if (position < text.length() && text.charAt(position) == '^') {
            position++;
            return new BinaryExpression(base, parseUnary(), Operation.POWER);
        }
        return base;
    }

    // факторіал сильніший за степінь: 2^3! = 2^6
    private Expression parseFactorial() {
        Expression result = parsePrimary();
        while (position < text.length() && text.charAt(position) == '!') {
            position++;
            result = new FactorialExpression(result);
        }
        return result;
    }

    private Expression parsePrimary() {
        if (position >= text.length()) {
            throw new CalculatorException("Unexpected end of expression");
        }
        char symbol = text.charAt(position);
        if (symbol == '(') {
            return parseBrackets();
        }
        if (Character.isDigit(symbol) || symbol == '.') {
            return parseNumber();
        }
        if (symbol == 'π') {
            position++;
            return new NumberExpression(Math.PI);
        }
        if (symbol == 'e') {
            position++;
            return new NumberExpression(Math.E);
        }
        // після назви функції одразу йде дужка, наприклад "sin("
        for (MathFunction function : MathFunction.values()) {
            if (text.startsWith(function.getSymbol() + "(", position)) {
                position += function.getSymbol().length();
                return new FunctionExpression(function, parseBrackets(),
                        angleUnit);
            }
        }
        throw unexpectedSymbol();
    }

    private Expression parseBrackets() {
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

    private Expression parseNumber() {
        int start = position;
        while (position < text.length()
                && (Character.isDigit(text.charAt(position))
                || text.charAt(position) == '.')) {
            position++;
        }
        // запис з порядком, наприклад 1.5E+20 або 2E-7
        if (position < text.length() && text.charAt(position) == 'E') {
            position++;
            if (position < text.length() && isSign(text.charAt(position))) {
                position++;
            }
            while (position < text.length()
                    && Character.isDigit(text.charAt(position))) {
                position++;
            }
        }
        String number = text.substring(start, position).replace('−', '-');
        try {
            return new NumberExpression(Double.parseDouble(number));
        } catch (NumberFormatException e) {
            throw new CalculatorException("Wrong number " + number, e);
        }
    }

    private boolean isSign(char symbol) {
        return symbol == '+' || symbol == '-' || symbol == '−';
    }

    private CalculatorException unexpectedSymbol() {
        return new CalculatorException("Unexpected symbol '"
                + text.charAt(position) + "' at position " + position);
    }
}

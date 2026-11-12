package ua.knu.calculator.expression;

// частина виразу, яка вміє себе обчислити (патерн Interpreter)
public interface Expression {

    double evaluate();
}

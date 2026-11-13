package ua.knu.calculator.expression;

public enum MathFunction {
    SIN("sin", true) {
        @Override
        public double apply(double argument) {
            return roundToZero(Math.sin(argument));
        }
    },
    COS("cos", true) {
        @Override
        public double apply(double argument) {
            return roundToZero(Math.cos(argument));
        }
    },
    TAN("tan", true) {
        @Override
        public double apply(double argument) {
            if (Math.abs(Math.cos(argument)) < ZERO) {
                throw new CalculatorException("Tangent is not defined");
            }
            return roundToZero(Math.tan(argument));
        }
    },
    LN("ln", false) {
        @Override
        public double apply(double argument) {
            if (argument <= 0) {
                throw new CalculatorException("Logarithm of non-positive");
            }
            return Math.log(argument);
        }
    },
    LOG("log", false) {
        @Override
        public double apply(double argument) {
            if (argument <= 0) {
                throw new CalculatorException("Logarithm of non-positive");
            }
            return Math.log10(argument);
        }
    },
    SQRT("√", false) {
        @Override
        public double apply(double argument) {
            if (argument < 0) {
                throw new CalculatorException("Root of negative number");
            }
            return Math.sqrt(argument);
        }
    };

    // менші за це числа вважаємо нулем, щоб sin(180°) був 0
    private static final double ZERO = 1e-12;

    private final String symbol;
    private final boolean trigonometric;

    MathFunction(String symbol, boolean trigonometric) {
        this.symbol = symbol;
        this.trigonometric = trigonometric;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isTrigonometric() {
        return trigonometric;
    }

    public abstract double apply(double argument);

    private static double roundToZero(double value) {
        if (Math.abs(value) < ZERO) {
            return 0;
        }
        return value;
    }
}

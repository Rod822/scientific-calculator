package ua.knu.calculator.expression;

public enum Operation {
    ADD {
        @Override
        public double apply(double left, double right) {
            return left + right;
        }
    },
    SUBTRACT {
        @Override
        public double apply(double left, double right) {
            return left - right;
        }
    },
    MULTIPLY {
        @Override
        public double apply(double left, double right) {
            return left * right;
        }
    },
    DIVIDE {
        @Override
        public double apply(double left, double right) {
            if (right == 0) {
                throw new CalculatorException("Division by zero");
            }
            return left / right;
        }
    },
    POWER {
        @Override
        public double apply(double left, double right) {
            return Math.pow(left, right);
        }
    };

    public abstract double apply(double left, double right);
}

package ua.knu.calculator.expression;

// одиниці вимірювання кутів для тригонометричних функцій
public enum AngleUnit {
    DEGREES {
        @Override
        public double toRadians(double angle) {
            return Math.toRadians(angle);
        }
    },
    RADIANS {
        @Override
        public double toRadians(double angle) {
            return angle;
        }
    };

    public abstract double toRadians(double angle);
}

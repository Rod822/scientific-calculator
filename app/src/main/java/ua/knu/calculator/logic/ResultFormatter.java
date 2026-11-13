package ua.knu.calculator.logic;

import java.math.BigDecimal;
import java.math.MathContext;

public class ResultFormatter {

    private static final int DIGITS = 12;
    private static final double MAX_PLAIN = 1e12;
    private static final double MIN_PLAIN = 1e-6;

    public String format(double value) {
        // так і −0 показується як 0
        if (value == 0) {
            return "0";
        }
        // 12 значущих цифр прибирають похибку: 0.1+0.2 дає 0.3
        BigDecimal number = new BigDecimal(value, new MathContext(DIGITS))
                .stripTrailingZeros();
        double rounded = Math.abs(number.doubleValue());
        if (rounded >= MAX_PLAIN || rounded < MIN_PLAIN) {
            return number.toString();
        }
        return number.toPlainString();
    }
}

package ua.knu.calculator.logic;

import java.util.ArrayList;
import java.util.List;

// вираз з екрана зберігається частинами, тому ⌫ стирає "sin(" цілком
public class ExpressionInput {

    private final ArrayList<String> tokens = new ArrayList<>();

    public void add(String token) {
        tokens.add(token);
    }

    public void removeLast() {
        if (!tokens.isEmpty()) {
            tokens.remove(tokens.size() - 1);
        }
    }

    public void clear() {
        tokens.clear();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    // після "=" результат стає початком нового виразу
    public void replaceWith(String token) {
        tokens.clear();
        tokens.add(token);
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        for (String token : tokens) {
            text.append(token);
        }
        return text.toString();
    }

    public ArrayList<String> getTokens() {
        return new ArrayList<>(tokens);
    }

    public void setTokens(List<String> newTokens) {
        tokens.clear();
        tokens.addAll(newTokens);
    }
}

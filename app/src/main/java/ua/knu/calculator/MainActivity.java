package ua.knu.calculator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import timber.log.Timber;

import ua.knu.calculator.expression.AngleUnit;
import ua.knu.calculator.expression.CalculatorException;
import ua.knu.calculator.logic.Calculator;
import ua.knu.calculator.logic.ExpressionInput;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_TOKENS = "tokens";
    private static final String KEY_PREVIOUS = "previous";
    private static final String KEY_ANGLE_UNIT = "angle_unit";
    private static final String KEY_ERROR = "error";

    private final Calculator calculator = new Calculator();
    private final ExpressionInput input = new ExpressionInput();

    private TextView previousText;
    private TextView inputText;
    private Button angleButton;
    private boolean errorShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right,
                            bars.bottom);
                    return insets;
                });

        previousText = findViewById(R.id.previous_text);
        inputText = findViewById(R.id.input_text);
        angleButton = findViewById(R.id.button_angle);

        GridLayout buttons = findViewById(R.id.buttons);
        for (int i = 0; i < buttons.getChildCount(); i++) {
            View button = buttons.getChildAt(i);
            // кнопки з tag просто дописують його у вираз
            if (button.getTag() != null) {
                button.setOnClickListener(this::onInputClick);
            }
        }
        findViewById(R.id.button_clear).setOnClickListener(v -> clear());
        findViewById(R.id.button_delete)
                .setOnClickListener(v -> deleteLast());
        findViewById(R.id.button_equals)
                .setOnClickListener(v -> calculate());
        angleButton.setOnClickListener(v -> switchAngleUnit());

        // після повороту екрана відновлюємо те, що було введено
        if (savedInstanceState != null) {
            input.setTokens(savedInstanceState.getStringArrayList(KEY_TOKENS));
            previousText.setText(savedInstanceState.getString(KEY_PREVIOUS));
            calculator.setAngleUnit(AngleUnit.valueOf(
                    savedInstanceState.getString(KEY_ANGLE_UNIT)));
            // текст помилки беремо заново, бо мова могла змінитися
            errorShown = savedInstanceState.getBoolean(KEY_ERROR);
            if (errorShown) {
                previousText.setText(R.string.error);
            }
        }
        showInput();
        showAngleUnit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_TOKENS, input.getTokens());
        outState.putString(KEY_PREVIOUS, previousText.getText().toString());
        outState.putString(KEY_ANGLE_UNIT, calculator.getAngleUnit().name());
        outState.putBoolean(KEY_ERROR, errorShown);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_english) {
            changeLanguage("en");
            return true;
        }
        if (item.getItemId() == R.id.menu_ukrainian) {
            changeLanguage("uk");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage(String language) {
        Timber.i("Language changed to %s", language);
        // активність перестворюється, а введене відновлюється з Bundle
        AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(language));
    }

    private void onInputClick(View button) {
        input.add((String) button.getTag());
        showInput();
    }

    private void clear() {
        input.clear();
        previousText.setText("");
        errorShown = false;
        showInput();
    }

    private void deleteLast() {
        input.removeLast();
        showInput();
    }

    private void calculate() {
        if (input.isEmpty()) {
            return;
        }
        String expression = input.getText();
        try {
            String result = calculator.calculate(expression);
            Timber.d("%s = %s", expression, result);
            previousText.setText(
                    getString(R.string.expression_equals, expression));
            input.replaceWith(result);
            errorShown = false;
        } catch (CalculatorException e) {
            Timber.w("Cannot calculate %s: %s", expression, e.getMessage());
            previousText.setText(R.string.error);
            errorShown = true;
        }
        showInput();
    }

    private void switchAngleUnit() {
        if (calculator.getAngleUnit() == AngleUnit.DEGREES) {
            calculator.setAngleUnit(AngleUnit.RADIANS);
        } else {
            calculator.setAngleUnit(AngleUnit.DEGREES);
        }
        Timber.i("Angle unit changed to %s", calculator.getAngleUnit());
        showAngleUnit();
    }

    private void showInput() {
        inputText.setText(input.getText());
    }

    private void showAngleUnit() {
        if (calculator.getAngleUnit() == AngleUnit.DEGREES) {
            angleButton.setText(R.string.degrees);
        } else {
            angleButton.setText(R.string.radians);
        }
    }
}

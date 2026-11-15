package ua.knu.calculator;

import android.app.Application;

import timber.log.Timber;

public class CalculatorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // логи пишуться тільки в debug-збірці, у release їх немає
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
